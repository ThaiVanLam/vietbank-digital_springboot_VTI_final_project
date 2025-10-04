package com.vietbank.vietbank_digital.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO cho request cập nhật thông tin cá nhân của Customer
 * Không cho phép thay đổi: username, citizenId, dateOfBirth
 */
@Data
public class UpdateCustomerProfileRequestDTO {
    @NotBlank(message = "{validation.customer.fullName.notBlank}")
    @Size(min = 2, max = 100, message = "{validation.customer.fullName.size}")
    private String fullName;

    @NotBlank(message = "{validation.customer.phoneNumber.notBlank}")
    @Pattern(regexp = "^(\\+84|0)[0-9]{9,10}$", message = "{validation.customer.phoneNumber.pattern}")
    private String phoneNumber;

    @NotBlank(message = "{validation.customer.email.notBlank}")
    @Email(message = "{validation.customer.email.invalid}")
    private String email;

    @NotBlank(message = "{validation.customer.address.notBlank}")
    @Size(max = 255, message = "{validation.customer.address.size}")
    private String address;

    @Size(max = 100, message = "{validation.customer.occupation.size}")
    private String occupation;
}
