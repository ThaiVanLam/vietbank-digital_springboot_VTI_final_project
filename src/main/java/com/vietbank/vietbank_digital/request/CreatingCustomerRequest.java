package com.vietbank.vietbank_digital.request;

import com.vietbank.vietbank_digital.model.BankAccount;
import com.vietbank.vietbank_digital.model.Customer;
import com.vietbank.vietbank_digital.model.User;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreatingCustomerRequest {
    private Long customerId;
    private String citizenId;
    private String address;
    private User user;
    private LocalDate dateOfBirth;
    private Customer.Gender gender;
    private String occupation;

    @Data
    public static class User {
        private String phoneNumber;
        private String username;
        private String fullName;
        private String email;

    }
}
