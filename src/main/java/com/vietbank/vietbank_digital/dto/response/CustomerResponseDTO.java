package com.vietbank.vietbank_digital.dto.response;

import com.vietbank.vietbank_digital.model.Customer;
import com.vietbank.vietbank_digital.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO cho response thông tin khách hàng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private Long customerId;
    private String fullName;
    private String phoneNumber;
    private String email;
    private String citizenId;
    private String address;
    private LocalDate dateOfBirth;
    private Customer.Gender gender;
    private String occupation;
    private String username;
    private User.Status status;
    private LocalDateTime createdAt;
    private Integer totalAccounts;
}
