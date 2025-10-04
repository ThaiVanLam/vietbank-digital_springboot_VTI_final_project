package com.vietbank.vietbank_digital.controller;

import com.vietbank.vietbank_digital.dto.request.CreateAccountRequestDTO;
import com.vietbank.vietbank_digital.dto.request.DepositRequestDTO;
import com.vietbank.vietbank_digital.dto.request.UpdateAccountStatusRequestDTO;
import com.vietbank.vietbank_digital.dto.response.BankAccountResponseDTO;
import com.vietbank.vietbank_digital.model.BankAccount;
import com.vietbank.vietbank_digital.security.services.UserDetailsImpl;
import com.vietbank.vietbank_digital.service.BankAccountService;
import com.vietbank.vietbank_digital.specification.BankAccountSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller cho Staff quản lý tài khoản ngân hàng
 */
@RestController
@RequestMapping("/api/v1/staff/accounts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class StaffBankAccountController {
    private final BankAccountService bankAccountService;

    /**
     * Tạo tài khoản ngân hàng mới
     * POST /api/v1/staff/accounts
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createAccount(
            @Valid @RequestBody CreateAccountRequestDTO requestDTO,
            Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        BankAccountResponseDTO createdAccount = bankAccountService.createAccount(requestDTO, userDetails.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "success.account.created");
        response.put("data", createdAccount);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Xem chi tiết tài khoản
     * GET /api/v1/staff/accounts/{accountId}
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<BankAccountResponseDTO> getAccountById(@PathVariable Long accountId) {
        BankAccountResponseDTO account = bankAccountService.getAccountById(accountId);
        return ResponseEntity.ok(account);
    }

    /**
     * Danh sách tài khoản của customer hoặc tìm kiếm
     * GET /api/v1/staff/accounts?customerId={id}&accountType=SAVINGS&status=ACTIVE
     */
    @GetMapping
    public ResponseEntity<Page<BankAccountResponseDTO>> searchAccounts(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) BankAccount.AccountType accountType,
            @RequestParam(required = false) BankAccount.Status status,
            @RequestParam(required = false) String customerName,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        // Build Specification
        Specification<BankAccount> spec = Specification.where(null);

        if (customerId != null) {
            spec = spec.and(BankAccountSpecification.hasCustomerId(customerId));
        }
        if (accountNumber != null && !accountNumber.trim().isEmpty()) {
            spec = spec.and(BankAccountSpecification.hasAccountNumber(accountNumber));
        }
        if (accountType != null) {
            spec = spec.and(BankAccountSpecification.hasAccountType(accountType));
        }
        if (status != null) {
            spec = spec.and(BankAccountSpecification.hasStatus(status));
        }
        if (customerName != null && !customerName.trim().isEmpty()) {
            spec = spec.and(BankAccountSpecification.hasCustomerNameContaining(customerName));
        }

        Page<BankAccountResponseDTO> accounts = bankAccountService.searchAccounts(spec, pageable);
        return ResponseEntity.ok(accounts);
    }

    /**
     * Nộp tiền vào tài khoản
     * PUT /api/v1/staff/accounts/{accountId}/deposit
     */
    @PutMapping("/{accountId}/deposit")
    public ResponseEntity<Map<String, Object>> deposit(
            @PathVariable Long accountId,
            @Valid @RequestBody DepositRequestDTO requestDTO,
            Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        BankAccountResponseDTO updatedAccount = bankAccountService.deposit(accountId, requestDTO, userDetails.getId());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "success.account.deposited");
        response.put("data", updatedAccount);

        return ResponseEntity.ok(response);
    }

    /**
     * Thay đổi trạng thái tài khoản (vô hiệu hóa)
     * PUT /api/v1/staff/accounts/{accountId}/status
     */
    @PutMapping("/{accountId}/status")
    public ResponseEntity<Map<String, Object>> updateAccountStatus(
            @PathVariable Long accountId,
            @Valid @RequestBody UpdateAccountStatusRequestDTO requestDTO,
            Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        BankAccountResponseDTO updatedAccount = bankAccountService.updateAccountStatus(
                accountId,
                requestDTO.getStatus(),
                userDetails.getId()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("message", "success.account.updated");
        response.put("data", updatedAccount);

        return ResponseEntity.ok(response);
    }
}
