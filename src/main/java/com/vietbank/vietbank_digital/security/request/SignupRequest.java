package com.vietbank.vietbank_digital.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String fullName;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    private String department;

    @NotBlank
    private String position;
}
