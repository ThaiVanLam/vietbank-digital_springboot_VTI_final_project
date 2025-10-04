package com.vietbank.vietbank_digital.dto.request;

import com.vietbank.vietbank_digital.model.Customer;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO cho request tạo/cập nhật khách hàng
 */
@Data
public class CustomerRequestDTO {
    @NotBlank(message = "{validation.customer.fullName.notBlank}")
    @Size(min = 2, max = 100, message = "{validation.customer.fullName.size}")
    private String fullName;

    @NotBlank(message = "{validation.customer.phoneNumber.notBlank}")
    @Pattern(regexp = "^(\\+84|0)[0-9]{9,10}$", message = "{validation.customer.phoneNumber.pattern}")
    private String phoneNumber;

    @NotBlank(message = "{validation.customer.email.notBlank}")
    @Email(message = "{validation.customer.email.invalid}")
    private String email;

    @NotBlank(message = "{validation.customer.citizenId.notBlank}")
    @Pattern(regexp = "^[0-9]{9,12}$", message = "{validation.customer.citizenId.pattern}")
    private String citizenId;

    @NotBlank(message = "{validation.customer.address.notBlank}")
    @Size(max = 255, message = "{validation.customer.address.size}")
    private String address;

    @NotNull(message = "{validation.customer.dateOfBirth.notNull}")
    @Past(message = "{validation.customer.dateOfBirth.past}")
    private LocalDate dateOfBirth;

    @NotNull(message = "{validation.customer.gender.notNull}")
    private Customer.Gender gender;

    @Size(max = 100, message = "{validation.customer.occupation.size}")
    private String occupation;

    @NotBlank(message = "{validation.customer.username.notBlank}")
    @Size(min = 3, max = 20, message = "{validation.customer.username.size}")
    private String username;
}
