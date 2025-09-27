package com.vietbank.vietbank_digital.controller;


import com.vietbank.vietbank_digital.model.AppRole;
import com.vietbank.vietbank_digital.model.Role;
import com.vietbank.vietbank_digital.model.Staff;
import com.vietbank.vietbank_digital.model.User;
import com.vietbank.vietbank_digital.repository.RoleRepository;
import com.vietbank.vietbank_digital.repository.StaffRepository;
import com.vietbank.vietbank_digital.repository.UserRepository;
import com.vietbank.vietbank_digital.security.jwt.JwtUtils;
import com.vietbank.vietbank_digital.security.request.LoginRequest;
import com.vietbank.vietbank_digital.security.request.SignupRequest;
import com.vietbank.vietbank_digital.security.response.MessageResponse;
import com.vietbank.vietbank_digital.security.response.UserInfoResponse;
import com.vietbank.vietbank_digital.security.services.UserDetailsImpl;
import com.vietbank.vietbank_digital.service.EmployeeCodeGenerator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmployeeCodeGenerator employeeCodeGenerator;

    @Autowired
    private StaffRepository staffRepository;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);

            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        UserInfoResponse loginResponse = new UserInfoResponse(userDetails.getId(), jwtCookie.toString(), userDetails.getUsername(), roles);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(loginResponse);
    }

    @PostMapping("/staff/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
        }

        User user = new User(signupRequest.getUsername(), signupRequest.getPhoneNumber(), signupRequest.getFullName(), signupRequest.getEmail(), passwordEncoder.encode(signupRequest.getPassword()));

        Role staffRole = roleRepository.findByRoleName(AppRole.ROLE_STAFF).orElseThrow(() -> new RuntimeException("Error: Role is not found!"));

        user.setRole(staffRole);

        Staff staff = new Staff(user.getId(), user, employeeCodeGenerator.generateSequentialUniqueCode(), signupRequest.getDepartment(), signupRequest.getPosition());

        staffRepository.save(staff);
        return ResponseEntity.ok(new MessageResponse("Staff registered successfully"));
    }

    @GetMapping("/username")
    public String currentUserName(Authentication authentication) {
        if (authentication != null) {
            return authentication.getName();
        } else return "";
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), roles);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new MessageResponse("You've been signed out!"));
    }
}
