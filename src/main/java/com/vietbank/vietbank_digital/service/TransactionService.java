package com.vietbank.vietbank_digital.service;

import com.vietbank.vietbank_digital.dto.request.TransferRequestDTO;
import com.vietbank.vietbank_digital.dto.response.TransactionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface cho Transaction Service
 */
public interface TransactionService {
    /**
     * Thực hiện chuyển khoản
     *
     * @param requestDTO - Thông tin chuyển khoản
     * @param customerId - ID của khách hàng thực hiện
     * @return TransactionResponseDTO
     */
    TransactionResponseDTO transfer(TransferRequestDTO requestDTO, Long customerId);

    /**
     * Lấy lịch sử giao dịch của tài khoản
     *
     * @param accountId - ID tài khoản
     * @param customerId - ID khách hàng (để verify ownership)
     * @param pageable - Phân trang
     * @return Page của TransactionResponseDTO
     */
    Page<TransactionResponseDTO> getTransactionsByAccountId(Long accountId, Long customerId, Pageable pageable);

    /**
     * Lấy chi tiết giao dịch
     *
     * @param transactionId - ID giao dịch
     * @param customerId - ID khách hàng (để verify ownership)
     * @return TransactionResponseDTO
     */
    TransactionResponseDTO getTransactionByIdAndCustomerId(Long transactionId, Long customerId);

    /**
     * Lấy giao dịch theo transaction number
     *
     * @param transactionNumber - Mã giao dịch
     * @return TransactionResponseDTO
     */
    TransactionResponseDTO getTransactionByNumber(String transactionNumber);
}
