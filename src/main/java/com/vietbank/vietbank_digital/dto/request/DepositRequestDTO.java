package com.vietbank.vietbank_digital.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO cho request nộp tiền vào tài khoản
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositRequestDTO {
    /**
     * Số tiền nộp (VND)
     */
    @NotNull(message = "{validation.transaction.amount.notNull}")
    @DecimalMin(value = "1000", message = "{validation.transaction.amount.min}")
    private BigDecimal amount;

    /**
     * Mô tả giao dịch
     */
    @Size(max = 255, message = "{validation.transaction.description.size}")
    private String description;
}
