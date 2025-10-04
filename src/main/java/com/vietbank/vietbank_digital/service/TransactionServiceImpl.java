package com.vietbank.vietbank_digital.service;

import com.vietbank.vietbank_digital.config.exception.InsufficientBalanceException;
import com.vietbank.vietbank_digital.config.exception.ResourceNotFoundException;
import com.vietbank.vietbank_digital.config.exception.TransactionFailedException;
import com.vietbank.vietbank_digital.config.exception.UnauthorizedException;
import com.vietbank.vietbank_digital.dto.request.TransferRequestDTO;
import com.vietbank.vietbank_digital.dto.response.TransactionResponseDTO;
import com.vietbank.vietbank_digital.model.BankAccount;
import com.vietbank.vietbank_digital.model.Transaction;
import com.vietbank.vietbank_digital.repository.BankAccountRepository;
import com.vietbank.vietbank_digital.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Implementation của TransactionService
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{
    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;

    @Override
    @Transactional
    public TransactionResponseDTO transfer(TransferRequestDTO requestDTO, Long customerId) {
        // Kiểm tra tài khoản nguồn
        BankAccount fromAccount = bankAccountRepository.findByAccountNumber(requestDTO.getFromAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("error.account.notFound", requestDTO.getFromAccountNumber()));

        // Verify ownership
        if (!fromAccount.getCustomer().getCustomerId().equals(customerId)) {
            throw new UnauthorizedException("error.unauthorized");
        }

        // Kiểm tra tài khoản đích
        BankAccount toAccount = bankAccountRepository.findByAccountNumber(requestDTO.getToAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("error.account.notFound", requestDTO.getToAccountNumber()));

        // Kiểm tra không thể chuyển cho chính mình
        if (fromAccount.getId().equals(toAccount.getId())) {
            throw new TransactionFailedException("error.transfer.sameAccount");
        }

        // Kiểm tra trạng thái tài khoản
        if (fromAccount.getStatus() != BankAccount.Status.ACTIVE) {
            throw new TransactionFailedException("error.account.inactive");
        }
        if (toAccount.getStatus() != BankAccount.Status.ACTIVE) {
            throw new TransactionFailedException("error.transfer.recipientInactive");
        }

        // Kiểm tra số dư
        if (fromAccount.getBalance().compareTo(requestDTO.getAmount()) < 0) {
            throw new InsufficientBalanceException();
        }

        // Thực hiện chuyển khoản
        fromAccount.setBalance(fromAccount.getBalance().subtract(requestDTO.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(requestDTO.getAmount()));
        fromAccount.setUpdatedAt(LocalDateTime.now());
        toAccount.setUpdatedAt(LocalDateTime.now());

        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);

        // Tạo transaction
        Transaction transaction = new Transaction();
        transaction.setTransactionNumber(generateTransactionNumber());
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setTransactionType(Transaction.TransactionType.TRANSFER);
        transaction.setAmount(requestDTO.getAmount());
        transaction.setCurrency("VND");
        transaction.setDescription(requestDTO.getDescription() != null
                ? requestDTO.getDescription()
                : "Transfer from " + fromAccount.getAccountNumber() + " to " + toAccount.getAccountNumber());
        transaction.setStatus(Transaction.Status.COMPLETED);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setCompletedDate(LocalDateTime.now());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setCreatedBy(customerId);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return convertToResponseDTO(savedTransaction, fromAccount.getBalance());
    }

    @Override
    public Page<TransactionResponseDTO> getTransactionsByAccountId(Long accountId, Long customerId, Pageable pageable) {
        // Kiểm tra tài khoản
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("error.account.notFound.byId", accountId));

        // Verify ownership
        if (!account.getCustomer().getCustomerId().equals(customerId)) {
            throw new UnauthorizedException("error.unauthorized");
        }

        // Lấy tất cả giao dịch liên quan đến tài khoản này
        Page<Transaction> transactions = transactionRepository.findAll(
                (root, query, cb) -> cb.or(
                        cb.equal(root.get("fromAccount").get("id"), accountId),
                        cb.equal(root.get("toAccount").get("id"), accountId)
                ),
                pageable
        );

        return transactions.map(tx -> convertToResponseDTO(tx, null));
    }

    @Override
    public TransactionResponseDTO getTransactionByIdAndCustomerId(Long transactionId, Long customerId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("error.transaction.notFound", transactionId));

        // Verify ownership - khách hàng phải là chủ tài khoản nguồn hoặc đích
        boolean isOwner = false;
        if (transaction.getFromAccount() != null) {
            isOwner = transaction.getFromAccount().getCustomer().getCustomerId().equals(customerId);
        }
        if (!isOwner && transaction.getToAccount() != null) {
            isOwner = transaction.getToAccount().getCustomer().getCustomerId().equals(customerId);
        }

        if (!isOwner) {
            throw new UnauthorizedException("error.unauthorized");
        }

        return convertToResponseDTO(transaction, null);
    }

    @Override
    public TransactionResponseDTO getTransactionByNumber(String transactionNumber) {
        Transaction transaction = transactionRepository.findByTransactionNumber(transactionNumber)
                .orElseThrow(() -> new ResourceNotFoundException("error.transaction.notFound", transactionNumber));
        return convertToResponseDTO(transaction, null);
    }

    // ==================== PRIVATE METHODS ====================

    /**
     * Tạo mã giao dịch duy nhất
     */
    private String generateTransactionNumber() {
        String prefix = "TXN";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.format("%04d", new Random().nextInt(10000));
        return prefix + timestamp + random;
    }

    /**
     * Convert Transaction entity to ResponseDTO
     */
    private TransactionResponseDTO convertToResponseDTO(Transaction transaction, BigDecimal balanceAfter) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(transaction.getId());
        dto.setTransactionNumber(transaction.getTransactionNumber());

        if (transaction.getFromAccount() != null) {
            dto.setFromAccountNumber(transaction.getFromAccount().getAccountNumber());
            dto.setFromAccountName(transaction.getFromAccount().getCustomer().getUser().getFullName());
        }

        if (transaction.getToAccount() != null) {
            dto.setToAccountNumber(transaction.getToAccount().getAccountNumber());
            dto.setToAccountName(transaction.getToAccount().getCustomer().getUser().getFullName());
        }

        dto.setTransactionType(transaction.getTransactionType());
        dto.setAmount(transaction.getAmount());
        dto.setCurrency(transaction.getCurrency());
        dto.setDescription(transaction.getDescription());
        dto.setStatus(transaction.getStatus());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setCompletedDate(transaction.getCompletedDate());
        dto.setBalanceAfter(balanceAfter);

        return dto;
    }
}
