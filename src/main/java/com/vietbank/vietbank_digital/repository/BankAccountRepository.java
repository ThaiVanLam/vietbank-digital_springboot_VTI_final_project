package com.vietbank.vietbank_digital.repository;

import com.vietbank.vietbank_digital.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long>, JpaSpecificationExecutor<BankAccount> {
    Optional<BankAccount> findByAccountNumber(String accountNumber);
    List<BankAccount> findByCustomerCustomerId(Long customerId);
    boolean existsByAccountNumber(String accountNumber);
}
