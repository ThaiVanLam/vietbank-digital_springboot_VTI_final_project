package com.vietbank.vietbank_digital.service;

import com.vietbank.vietbank_digital.model.Customer;
import com.vietbank.vietbank_digital.request.CreatingCustomerRequest;

public interface CustomerService {
    Customer getCustomerById(Long profileId);

    void addCustomer(CreatingCustomerRequest creatingCustomerRequest);
}
