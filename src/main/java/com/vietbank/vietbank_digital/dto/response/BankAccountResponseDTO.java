package com.vietbank.vietbank_digital.dto.response;

import com.vietbank.vietbank_digital.model.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO cho response thông tin tài khoản ngân hàng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountResponseDTO {
    private Long id;
    private String accountNumber;
    private Long customerId;
    private String customerName;
    private BankAccount.AccountType accountType;
    private BigDecimal balance;
    private String currency;
    private BankAccount.Status status;
    private LocalDate openedDate;
    private LocalDate closedDate;
    private BigDecimal interestRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
