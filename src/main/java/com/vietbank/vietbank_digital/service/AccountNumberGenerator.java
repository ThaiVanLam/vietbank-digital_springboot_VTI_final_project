package com.vietbank.vietbank_digital.service;

import com.vietbank.vietbank_digital.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

/**
 * Service để tạo số tài khoản ngân hàng duy nhất
 */
@Service
@RequiredArgsConstructor
public class AccountNumberGenerator {
    private final BankAccountRepository bankAccountRepository;
    private static final String PREFIX = "VTB"; // VietBank prefix
    private static final Random random = new Random();

    /**
     * Tạo số tài khoản dạng: VTBxxxxxxxxx (12 chữ số)
     * VTB: prefix ngân hàng
     * xxxxxxxxx: 9 chữ số ngẫu nhiên
     */
    @Transactional
    public String generateUniqueAccountNumber() {
        String accountNumber;
        int attempts = 0;
        int maxAttempts = 10;

        do {
            accountNumber = generateRandomAccountNumber();
            attempts++;

            if (attempts >= maxAttempts) {
                throw new RuntimeException("Failed to generate unique account number after " + maxAttempts + " attempts");
            }
        } while (bankAccountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }

    /**
     * Tạo số tài khoản ngẫu nhiên
     */
    private String generateRandomAccountNumber() {
        // Tạo 9 chữ số ngẫu nhiên
        long randomNumber = 100000000L + (long)(random.nextDouble() * 900000000L);
        return PREFIX + randomNumber;
    }

    /**
     * Tạo số tài khoản theo loại tài khoản
     * SAV: Savings (Tiết kiệm)
     * CUR: Current (Thanh toán)
     * FIX: Fixed Deposit (Tiền gửi có kỳ hạn)
     */
    public String generateAccountNumberByType(String accountType) {
        String typePrefix;
        switch (accountType.toUpperCase()) {
            case "SAVINGS":
                typePrefix = "SAV";
                break;
            case "CURRENT":
                typePrefix = "CUR";
                break;
            case "FIXED_DEPOSIT":
                typePrefix = "FIX";
                break;
            default:
                typePrefix = "GEN";
        }

        String accountNumber;
        do {
            // Format: VTBtttnnnnnnnn (12 ký tự)
            // ttt: loại tài khoản (SAV/CUR/FIX)
            // nnnnnnnn: 8 chữ số ngẫu nhiên
            long randomNumber = 10000000L + (long)(random.nextDouble() * 90000000L);
            accountNumber = PREFIX + typePrefix + randomNumber;
        } while (bankAccountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }
}
