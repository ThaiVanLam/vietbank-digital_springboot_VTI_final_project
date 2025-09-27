package com.vietbank.vietbank_digital.service;

import com.vietbank.vietbank_digital.model.Customer;
import com.vietbank.vietbank_digital.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;

    @Override
    public Customer getCustomerById(Long profileId) {
        return customerRepository.findCustomerById(profileId);
    }
}
