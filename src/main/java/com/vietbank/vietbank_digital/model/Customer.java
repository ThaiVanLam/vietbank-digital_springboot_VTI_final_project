package com.vietbank.vietbank_digital.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customers")
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Column(name = "citizen_id")
    private String citizenId;

    private String address;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String occupation;

    @Column(name = "created_by")
    private Long createdBy;
    @Column(name = "updated_by")
    private Long updatedBy;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<BankAccount> bankAccounts;

    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
