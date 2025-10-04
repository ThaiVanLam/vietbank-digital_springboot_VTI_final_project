package com.vietbank.vietbank_digital.service;

import com.vietbank.vietbank_digital.dto.request.CreateAccountRequestDTO;
import com.vietbank.vietbank_digital.dto.request.DepositRequestDTO;
import com.vietbank.vietbank_digital.dto.response.BankAccountResponseDTO;
import com.vietbank.vietbank_digital.model.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Interface cho Bank Account Service
 */
public interface BankAccountService {
    /**
     * Tạo tài khoản ngân hàng mới
     *
     * @param requestDTO - Thông tin tài khoản cần tạo
     * @param staffId - ID của staff thực hiện tạo
     * @return BankAccountResponseDTO
     */
    BankAccountResponseDTO createAccount(CreateAccountRequestDTO requestDTO, Long staffId);

    /**
     * Lấy chi tiết tài khoản theo ID
     *
     * @param accountId - ID của tài khoản
     * @return BankAccountResponseDTO
     */
    BankAccountResponseDTO getAccountById(Long accountId);

    /**
     * Lấy danh sách tài khoản của customer
     *
     * @param customerId - ID của customer
     * @param pageable - Thông tin phân trang
     * @return Page của BankAccountResponseDTO
     */
    Page<BankAccountResponseDTO> getAccountsByCustomerId(Long customerId, Pageable pageable);

    /**
     * Tìm kiếm tài khoản với Specification
     *
     * @param spec - Specification chứa điều kiện tìm kiếm
     * @param pageable - Thông tin phân trang
     * @return Page của BankAccountResponseDTO
     */
    Page<BankAccountResponseDTO> searchAccounts(Specification<BankAccount> spec, Pageable pageable);

    /**
     * Nộp tiền vào tài khoản
     *
     * @param accountId - ID của tài khoản
     * @param requestDTO - Thông tin nộp tiền
     * @param staffId - ID của staff thực hiện
     * @return BankAccountResponseDTO đã được cập nhật
     */
    BankAccountResponseDTO deposit(Long accountId, DepositRequestDTO requestDTO, Long staffId);

    /**
     * Thay đổi trạng thái tài khoản
     *
     * @param accountId - ID của tài khoản
     * @param status - Trạng thái mới
     * @param staffId - ID của staff thực hiện
     * @return BankAccountResponseDTO đã được cập nhật
     */
    BankAccountResponseDTO updateAccountStatus(Long accountId, BankAccount.Status status, Long staffId);

    /**
     * Kiểm tra tài khoản có tồn tại không
     *
     * @param accountId - ID cần kiểm tra
     * @return true nếu tồn tại
     */
    boolean existsById(Long accountId);

    /**
     * Lấy tài khoản theo số tài khoản
     *
     * @param accountNumber - Số tài khoản
     * @return BankAccountResponseDTO
     */
    BankAccountResponseDTO getAccountByAccountNumber(String accountNumber);

    /**
     * Lấy danh sách tài khoản của customer (List thay vì Page)
     *
     * @param customerId - ID của customer
     * @return List của BankAccountResponseDTO
     */
    List<BankAccountResponseDTO> getAccountsByCustomerId(Long customerId);

    /**
     * Lấy chi tiết tài khoản và verify ownership
     *
     * @param accountId - ID của tài khoản
     * @param customerId - ID của customer (để verify)
     * @return BankAccountResponseDTO
     */
    BankAccountResponseDTO getAccountByIdAndCustomerId(Long accountId, Long customerId);
}
