package com.vietbank.vietbank_digital.service;

import com.vietbank.vietbank_digital.config.exception.ResourceNotFoundException;
import com.vietbank.vietbank_digital.config.exception.TransactionFailedException;
import com.vietbank.vietbank_digital.config.exception.UnauthorizedException;
import com.vietbank.vietbank_digital.dto.request.CreateAccountRequestDTO;
import com.vietbank.vietbank_digital.dto.request.DepositRequestDTO;
import com.vietbank.vietbank_digital.dto.response.BankAccountResponseDTO;
import com.vietbank.vietbank_digital.model.BankAccount;
import com.vietbank.vietbank_digital.model.Customer;
import com.vietbank.vietbank_digital.model.Transaction;
import com.vietbank.vietbank_digital.repository.BankAccountRepository;
import com.vietbank.vietbank_digital.repository.CustomerRepository;
import com.vietbank.vietbank_digital.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Implementation của BankAccountService
 */
@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService{
    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public BankAccountResponseDTO createAccount(CreateAccountRequestDTO requestDTO, Long staffId) {
        // Kiểm tra customer tồn tại
        Customer customer = customerRepository.findById(requestDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("error.customer.notFound", requestDTO.getCustomerId()));

        // Tạo tài khoản mới
        BankAccount account = new BankAccount();
        account.setAccountNumber(generateAccountNumber());
        account.setCustomer(customer);
        account.setAccountType(requestDTO.getAccountType());
        account.setBalance(requestDTO.getInitialDeposit() != null ? requestDTO.getInitialDeposit() : BigDecimal.ZERO);
        account.setCurrency("VND");
        account.setStatus(BankAccount.Status.ACTIVE);
        account.setOpenedDate(LocalDate.now());
        account.setInterestRate(requestDTO.getInterestRate());
        account.setCreatedBy(staffId);
        account.setUpdatedAt(LocalDateTime.now());

        BankAccount savedAccount = bankAccountRepository.save(account);

        // Nếu có tiền gửi ban đầu, tạo transaction DEPOSIT
        if (requestDTO.getInitialDeposit() != null && requestDTO.getInitialDeposit().compareTo(BigDecimal.ZERO) > 0) {
            createDepositTransaction(savedAccount, requestDTO.getInitialDeposit(), "Initial deposit", staffId);
        }

        return convertToResponseDTO(savedAccount);
    }

    @Override
    public BankAccountResponseDTO getAccountById(Long accountId) {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("error.account.notFound", accountId));
        return convertToResponseDTO(account);
    }

    @Override
    public Page<BankAccountResponseDTO> getAccountsByCustomerId(Long customerId, Pageable pageable) {
        // Kiểm tra customer tồn tại
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("error.customer.notFound", customerId);
        }

        Page<BankAccount> accounts = bankAccountRepository.findAll(
                (root, query, cb) -> cb.equal(root.get("customer").get("customerId"), customerId),
                pageable
        );

        return accounts.map(this::convertToResponseDTO);
    }

    @Override
    public Page<BankAccountResponseDTO> searchAccounts(Specification<BankAccount> spec, Pageable pageable) {
        Page<BankAccount> accounts = bankAccountRepository.findAll(spec, pageable);
        return accounts.map(this::convertToResponseDTO);
    }

    @Override
    @Transactional
    public BankAccountResponseDTO deposit(Long accountId, DepositRequestDTO requestDTO, Long staffId) {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("error.account.notFound", accountId));

        // Kiểm tra trạng thái tài khoản
        if (account.getStatus() != BankAccount.Status.ACTIVE) {
            throw new TransactionFailedException("error.account.inactive");
        }

        // Cập nhật số dư
        BigDecimal newBalance = account.getBalance().add(requestDTO.getAmount());
        account.setBalance(newBalance);
        account.setUpdatedAt(LocalDateTime.now());
        account.setUpdatedBy(staffId);

        BankAccount updatedAccount = bankAccountRepository.save(account);

        // Tạo transaction DEPOSIT
        String description = requestDTO.getDescription() != null
                ? requestDTO.getDescription()
                : "Deposit to account " + account.getAccountNumber();
        createDepositTransaction(updatedAccount, requestDTO.getAmount(), description, staffId);

        return convertToResponseDTO(updatedAccount);
    }

    @Override
    @Transactional
    public BankAccountResponseDTO updateAccountStatus(Long accountId, BankAccount.Status status, Long staffId) {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("error.account.notFound", accountId));

        account.setStatus(status);
        account.setUpdatedAt(LocalDateTime.now());
        account.setUpdatedBy(staffId);

        // Nếu đóng tài khoản, cập nhật ngày đóng
        if (status == BankAccount.Status.CLOSED) {
            account.setClosedDate(LocalDate.now());
        }

        BankAccount updatedAccount = bankAccountRepository.save(account);
        return convertToResponseDTO(updatedAccount);
    }

    @Override
    public List<BankAccountResponseDTO> getAccountsByCustomerId(Long customerId) {
        // Kiểm tra customer tồn tại
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("error.customer.notFound", customerId);
        }

        List<BankAccount> accounts = bankAccountRepository.findByCustomerCustomerId(customerId);
        return accounts.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BankAccountResponseDTO getAccountByIdAndCustomerId(Long accountId, Long customerId) {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("error.account.notFound.byId", accountId));

        // Verify ownership
        if (!account.getCustomer().getCustomerId().equals(customerId)) {
            throw new UnauthorizedException("error.unauthorized");
        }

        return convertToResponseDTO(account);
    }

    @Override
    public boolean existsById(Long accountId) {
        return bankAccountRepository.existsById(accountId);
    }

    @Override
    public BankAccountResponseDTO getAccountByAccountNumber(String accountNumber) {
        BankAccount account = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("error.account.notFound", accountNumber));
        return convertToResponseDTO(account);
    }

    // ==================== PRIVATE METHODS ====================

    /**
     * Tạo số tài khoản ngẫu nhiên 12 chữ số
     */
    private String generateAccountNumber() {
        String accountNumber;
        do {
            // Tạo số tài khoản 12 chữ số
            accountNumber = String.format("%012d", new Random().nextLong() & Long.MAX_VALUE).substring(0, 12);
        } while (bankAccountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }

    /**
     * Tạo transaction DEPOSIT
     */
    private void createDepositTransaction(BankAccount account, BigDecimal amount, String description, Long staffId) {
        Transaction transaction = new Transaction();
        transaction.setTransactionNumber(generateTransactionNumber());
        transaction.setToAccount(account);
        transaction.setTransactionType(Transaction.TransactionType.DEPOSIT);
        transaction.setAmount(amount);
        transaction.setCurrency("VND");
        transaction.setDescription(description);
        transaction.setStatus(Transaction.Status.COMPLETED);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setCompletedDate(LocalDateTime.now());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setCreatedBy(staffId);

        transactionRepository.save(transaction);
    }

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
     * Convert BankAccount entity to ResponseDTO
     */
    private BankAccountResponseDTO convertToResponseDTO(BankAccount account) {
        BankAccountResponseDTO dto = new BankAccountResponseDTO();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setCustomerId(account.getCustomer().getCustomerId());
        dto.setCustomerName(account.getCustomer().getUser().getFullName());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        dto.setCurrency(account.getCurrency());
        dto.setStatus(account.getStatus());
        dto.setOpenedDate(account.getOpenedDate());
        dto.setClosedDate(account.getClosedDate());
        dto.setInterestRate(account.getInterestRate());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setUpdatedAt(account.getUpdatedAt());
        return dto;
    }
}
