package com.vietbank.vietbank_digital.dto.request;

import com.vietbank.vietbank_digital.model.BankAccount;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO cho request tạo tài khoản ngân hàng mới
 * Sử dụng bởi Staff khi tạo tài khoản cho khách hàng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestDTO {
    /**
     * ID của khách hàng sở hữu tài khoản
     */
    @NotNull(message = "{validation.account.customerId.notNull}")
    private Long customerId;

    /**
     * Loại tài khoản: SAVINGS, CURRENT, FIXED_DEPOSIT
     */
    @NotNull(message = "{validation.account.accountType.notNull}")
    private BankAccount.AccountType accountType;

    /**
     * Số tiền gửi ban đầu (VND)
     * Mặc định = 0 nếu không cung cấp
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "{validation.account.initialDeposit.min}")
    private BigDecimal initialDeposit = BigDecimal.ZERO;

    /**
     * Lãi suất (%/năm)
     * Ví dụ: 5.5 nghĩa là 5.5%
     */
    @DecimalMin(value = "0.0", message = "{validation.account.interestRate.min}")
    @DecimalMax(value = "100.0", message = "{validation.account.interestRate.max}")
    private BigDecimal interestRate;
}
