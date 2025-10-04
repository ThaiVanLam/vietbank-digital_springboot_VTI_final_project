package com.vietbank.vietbank_digital.dto.response;

import com.vietbank.vietbank_digital.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO cho response thông tin giao dịch
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {
    private Long id;
    private String transactionNumber;
    private String fromAccountNumber;
    private String fromAccountName;
    private String toAccountNumber;
    private String toAccountName;
    private Transaction.TransactionType transactionType;
    private BigDecimal amount;
    private String currency;
    private String description;
    private Transaction.Status status;
    private LocalDateTime transactionDate;
    private LocalDateTime completedDate;

    /**
     * Số dư sau giao dịch (nếu có)
     */
    private BigDecimal balanceAfter;
}
