package com.vietbank.vietbank_digital.dto.request;

import com.vietbank.vietbank_digital.model.BankAccount;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho request thay đổi trạng thái tài khoản
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountStatusRequestDTO {
    @NotNull(message = "Status is required")
    private BankAccount.Status status;
}
