package com.vietbank.vietbank_digital.controller;

import com.vietbank.vietbank_digital.dto.request.TransferRequestDTO;
import com.vietbank.vietbank_digital.dto.response.TransactionResponseDTO;
import com.vietbank.vietbank_digital.security.services.UserDetailsImpl;
import com.vietbank.vietbank_digital.service.CustomerService;
import com.vietbank.vietbank_digital.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller cho Customer thực hiện chuyển khoản và xem lịch sử giao dịch
 */
@RestController
@RequestMapping("/api/v1/customer/transactions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerTransferController {
    private final TransactionService transactionService;
    private final CustomerService customerService;

    /**
     * Chuyển khoản
     * POST /api/v1/customer/transactions/transfer
     */
    @PostMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transfer(
            @Valid @RequestBody TransferRequestDTO requestDTO,
            Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long customerId = customerService.getCustomerByUserId(userDetails.getId()).getCustomerId();

        TransactionResponseDTO transaction = transactionService.transfer(requestDTO, customerId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "success.transfer.completed");
        response.put("data", transaction);

        return ResponseEntity.ok(response);
    }

    /**
     * Xem lịch sử giao dịch của tài khoản
     * GET /api/v1/customer/transactions?accountId={accountId}
     */
    @GetMapping
    public ResponseEntity<Page<TransactionResponseDTO>> getTransactionHistory(
            @RequestParam Long accountId,
            @PageableDefault(size = 20, sort = "transactionDate", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long customerId = customerService.getCustomerByUserId(userDetails.getId()).getCustomerId();

        Page<TransactionResponseDTO> transactions = transactionService.getTransactionsByAccountId(
                accountId,
                customerId,
                pageable
        );

        return ResponseEntity.ok(transactions);
    }

    /**
     * Xem chi tiết giao dịch
     * GET /api/v1/customer/transactions/{transactionId}
     */
    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> getTransactionDetail(
            @PathVariable Long transactionId,
            Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long customerId = customerService.getCustomerByUserId(userDetails.getId()).getCustomerId();

        TransactionResponseDTO transaction = transactionService.getTransactionByIdAndCustomerId(
                transactionId,
                customerId
        );

        return ResponseEntity.ok(transaction);
    }
}
