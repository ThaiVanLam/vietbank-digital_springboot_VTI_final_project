package com.vietbank.vietbank_digital.controller;

import com.vietbank.vietbank_digital.request.CreatingCustomerRequest;
import com.vietbank.vietbank_digital.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/staff")
public class StaffController {
    @Autowired
    private CustomerService customerService;

    @PostMapping(name = "/customers")
    public void  addCustomer(@RequestBody CreatingCustomerRequest creatingCustomerRequest){
        customerService.addCustomer(creatingCustomerRequest);
    }
}
