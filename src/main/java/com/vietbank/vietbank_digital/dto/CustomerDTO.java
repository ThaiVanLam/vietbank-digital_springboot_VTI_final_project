package com.vietbank.vietbank_digital.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CustomerDTO {
    @NonNull
    private String fullName;

    @NonNull
    private String citizenId;
    @NonNull
    private String email;
    @NonNull
    private String address;

    @NonNull
    private LocalDate dateOfBirth;

    @NonNull
    private String gender;

    @NonNull
    private String occupation;

    @NonNull
    private String userPhoneNumber;
    @NonNull
    private String userPassword;

    @NonNull
    private String userRole;

    @NonNull
    private String userStatus;
}
