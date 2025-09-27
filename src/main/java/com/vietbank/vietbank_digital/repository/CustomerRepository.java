package com.vietbank.vietbank_digital.repository;

import com.vietbank.vietbank_digital.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findCustomerById(Long profileId);
}
