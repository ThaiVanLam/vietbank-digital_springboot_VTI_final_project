package com.vietbank.vietbank_digital.controller;

import com.vietbank.vietbank_digital.dto.request.UpdateCustomerProfileRequestDTO;
import com.vietbank.vietbank_digital.dto.response.CustomerResponseDTO;
import com.vietbank.vietbank_digital.security.services.UserDetailsImpl;
import com.vietbank.vietbank_digital.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller cho Customer quản lý thông tin cá nhân
 */
@RestController
@RequestMapping("/api/v1/customer/profile")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerProfileController {
    private final CustomerService customerService;

    /**
     * Xem thông tin cá nhân
     * GET /api/v1/customer/profile
     */
    @GetMapping
    public ResponseEntity<CustomerResponseDTO> getProfile(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        CustomerResponseDTO profile = customerService.getCustomerByUserId(userDetails.getId());
        return ResponseEntity.ok(profile);
    }

    /**
     * Cập nhật thông tin cá nhân
     * PUT /api/v1/customer/profile
     */
    @PutMapping
    public ResponseEntity<Map<String, Object>> updateProfile(
            @Valid @RequestBody UpdateCustomerProfileRequestDTO requestDTO,
            Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        CustomerResponseDTO customer = customerService.getCustomerByUserId(userDetails.getId());

        CustomerResponseDTO updatedProfile = customerService.updateCustomerProfile(
                customer.getCustomerId(),
                requestDTO
        );

        Map<String, Object> response = new HashMap<>();
        response.put("message", "success.customer.updated");
        response.put("data", updatedProfile);

        return ResponseEntity.ok(response);
    }
}
