package com.vietbank.vietbank_digital.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bank_accounts")
@Data
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private AccountType accountType = AccountType.SAVINGS;

    private BigDecimal balance = BigDecimal.ZERO;

    private String currency = "VND";

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(name = "opened_date")
    private LocalDate openedDate;
    @Column(name = "closed_date")
    private LocalDate closedDate;

    private BigDecimal interestRate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_by")
    private Long createdBy;
    @Column(name = "updated_by")
    private Long updatedBy;

    @OneToMany(mappedBy = "fromAccount")
    private List<Transaction> outgoingTransactions;

    @OneToMany(mappedBy = "toAccount")
    private List<Transaction> incomingTransactions;

    public enum AccountType {
        SAVINGS, CURRENT, FIXED_DEPOSIT
    }

    public enum Status {
        ACTIVE, INACTIVE, CLOSED, SUSPENDED
    }
}
