package com.vietbank.vietbank_digital.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO cho request chuyển khoản
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDTO {
    /**
     * Số tài khoản nguồn (của người chuyển)
     */
    @NotBlank(message = "{validation.transfer.fromAccount.notBlank}")
    private String fromAccountNumber;

    /**
     * Số tài khoản đích (người nhận)
     */
    @NotBlank(message = "{validation.transfer.toAccount.notBlank}")
    private String toAccountNumber;

    /**
     * Số tiền chuyển (VND)
     */
    @NotNull(message = "{validation.transaction.amount.notNull}")
    @DecimalMin(value = "10000", message = "{validation.transfer.amount.min}")
    @DecimalMax(value = "1000000000", message = "{validation.transfer.amount.max}")
    private BigDecimal amount;

    /**
     * Nội dung chuyển khoản
     */
    @Size(max = 255, message = "{validation.transaction.description.size}")
    private String description;

    /**
     * Mật khẩu giao dịch (nếu cần xác thực)
     */
    private String transactionPassword;
}
