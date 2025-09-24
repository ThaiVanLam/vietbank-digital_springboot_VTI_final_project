package com.vietbank.vietbank_digital.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_number", nullable = false, unique = true)
    private String transactionNumber;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private BankAccount fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private BankAccount toAccount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private BigDecimal amount;
    private String currency = "VND";
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;
    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;
    @Column(name = "updated_by")
    private Long updatedBy;

    public enum TransactionType {
        TRANSFER, DEPOSIT, WITHDRAWAL, PAYMENT
    }

    public enum Status {
        PENDING, COMPLETED, FAILED, CANCELLED
    }

}
