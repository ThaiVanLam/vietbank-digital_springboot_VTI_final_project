package com.vietbank.vietbank_digital.controller;

import com.vietbank.vietbank_digital.dto.response.BankAccountResponseDTO;
import com.vietbank.vietbank_digital.security.services.UserDetailsImpl;
import com.vietbank.vietbank_digital.service.BankAccountService;
import com.vietbank.vietbank_digital.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller cho Customer xem thông tin tài khoản
 */
@RestController
@RequestMapping("/api/v1/customer/accounts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerBankAccountController {
    private final BankAccountService bankAccountService;
    private final CustomerService customerService;

    /**
     * Xem danh sách tất cả tài khoản của khách hàng
     * GET /api/v1/customer/accounts
     */
    @GetMapping
    public ResponseEntity<List<BankAccountResponseDTO>> getMyAccounts(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long customerId = customerService.getCustomerByUserId(userDetails.getId()).getCustomerId();

        List<BankAccountResponseDTO> accounts = bankAccountService.getAccountsByCustomerId(customerId);
        return ResponseEntity.ok(accounts);
    }

    /**
     * Xem chi tiết một tài khoản cụ thể
     * GET /api/v1/customer/accounts/{accountId}
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<BankAccountResponseDTO> getAccountDetail(
            @PathVariable Long accountId,
            Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long customerId = customerService.getCustomerByUserId(userDetails.getId()).getCustomerId();

        BankAccountResponseDTO account = bankAccountService.getAccountByIdAndCustomerId(accountId, customerId);
        return ResponseEntity.ok(account);
    }

    /**
     * Xem số dư tài khoản
     * GET /api/v1/customer/accounts/{accountId}/balance
     */
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<?> getAccountBalance(
            @PathVariable Long accountId,
            Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long customerId = customerService.getCustomerByUserId(userDetails.getId()).getCustomerId();

        BankAccountResponseDTO account = bankAccountService.getAccountByIdAndCustomerId(accountId, customerId);

        return ResponseEntity.ok(new BalanceResponse(
                account.getAccountNumber(),
                account.getBalance(),
                account.getCurrency(),
                account.getAccountType()
        ));
    }

    /**
     * Inner class cho Balance Response
     */
    private record BalanceResponse(
            String accountNumber,
            java.math.BigDecimal balance,
            String currency,
            com.vietbank.vietbank_digital.model.BankAccount.AccountType accountType
    ) {}
}
