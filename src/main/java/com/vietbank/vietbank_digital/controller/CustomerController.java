package com.vietbank.vietbank_digital.controller;

import com.vietbank.vietbank_digital.dto.CustomerDTO;
import com.vietbank.vietbank_digital.model.Customer;
import com.vietbank.vietbank_digital.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    @GetMapping("/profile/{profileId}")
    public CustomerDTO getProfile(@PathVariable(name = "profileId") Long profileId){
        Customer customer = customerService.getCustomerById(profileId);

        CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
        return customerDTO;
    }




}
