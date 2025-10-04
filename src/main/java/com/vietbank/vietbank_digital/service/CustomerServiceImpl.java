package com.vietbank.vietbank_digital.service;

import com.vietbank.vietbank_digital.model.AppRole;
import com.vietbank.vietbank_digital.model.Customer;
import com.vietbank.vietbank_digital.model.Role;
import com.vietbank.vietbank_digital.model.User;
import com.vietbank.vietbank_digital.repository.CustomerRepository;
import com.vietbank.vietbank_digital.repository.RoleRepository;
import com.vietbank.vietbank_digital.request.CreatingCustomerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Customer getCustomerById(Long profileId) {
        return customerRepository.findCustomerById(profileId);
    }

    @Override
    public void addCustomer(CreatingCustomerRequest creatingCustomerRequest) {
        Customer customer = new Customer(creatingCustomerRequest.getCitizenId(), creatingCustomerRequest.getAddress(), creatingCustomerRequest.getDateOfBirth(), creatingCustomerRequest.getGender(), creatingCustomerRequest.getOccupation());


        User user = new User(creatingCustomerRequest.getUser().getUsername(), creatingCustomerRequest.getUser().getPhoneNumber(), creatingCustomerRequest.getUser().getFullName(), creatingCustomerRequest.getUser().getEmail(), passwordEncoder.encode("firstpassword"));

        Role role = roleRepository.findByRoleName(AppRole.ROLE_CUSTOMER).orElseThrow(() -> new RuntimeException("Error: Role is not found!"));
        user.setRole(role);

        customer.setUser(user);

        //customer set created by
    }
}
