package com.vietbank.vietbank_digital.service;

import com.vietbank.vietbank_digital.dto.request.CustomerRequestDTO;
import com.vietbank.vietbank_digital.dto.request.UpdateCustomerProfileRequestDTO;
import com.vietbank.vietbank_digital.dto.response.CustomerResponseDTO;
import com.vietbank.vietbank_digital.model.Customer;
import com.vietbank.vietbank_digital.request.CreatingCustomerRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * Interface cho Customer Service
 * Định nghĩa các business logic liên quan đến Customer
 */
public interface CustomerService {

    /**
     * Tìm kiếm khách hàng với Specification (động)
     *
     * @param spec - Specification chứa các điều kiện tìm kiếm
     * @param pageable - Thông tin phân trang và sắp xếp
     * @return Page của CustomerResponseDTO
     */
    Page<CustomerResponseDTO> searchCustomers(Specification<Customer> spec, Pageable pageable);

    /**
     * Lấy thông tin chi tiết khách hàng theo ID
     *
     * @param customerId - ID của khách hàng
     * @return CustomerResponseDTO
     * @throws ResourceNotFoundException nếu không tìm thấy
     */
    CustomerResponseDTO getCustomerById(Long customerId);

    /**
     * Tạo khách hàng mới
     * Sử dụng bởi Staff
     *
     * @param requestDTO - Thông tin khách hàng cần tạo
     * @return CustomerResponseDTO của khách hàng vừa tạo
     * @throws DuplicateResourceException nếu phone/email/citizenId đã tồn tại
     */
    CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO);

    /**
     * Cập nhật thông tin khách hàng
     * Sử dụng bởi Staff hoặc Customer (cập nhật chính mình)
     *
     * @param customerId - ID của khách hàng cần cập nhật
     * @param requestDTO - Thông tin mới
     * @return CustomerResponseDTO đã được cập nhật
     * @throws ResourceNotFoundException nếu không tìm thấy
     * @throws DuplicateResourceException nếu thông tin mới bị trùng với khách hàng khác
     */
    CustomerResponseDTO updateCustomer(Long customerId, CustomerRequestDTO requestDTO);

    /**
     * Xóa mềm khách hàng (cập nhật status = INACTIVE)
     * Sử dụng bởi Staff
     *
     * @param customerId - ID của khách hàng cần xóa
     * @throws ResourceNotFoundException nếu không tìm thấy
     */
    void deleteCustomer(Long customerId);

    /**
     * Lấy thông tin Customer theo User ID
     * Hữu ích khi cần lấy thông tin từ Authentication
     *
     * @param userId - ID của User
     * @return CustomerResponseDTO
     * @throws ResourceNotFoundException nếu không tìm thấy
     */
    CustomerResponseDTO getCustomerByUserId(Long userId);

    /**
     * Kiểm tra xem Customer có tồn tại không
     *
     * @param customerId - ID cần kiểm tra
     * @return true nếu tồn tại, false nếu không
     */
    boolean existsById(Long customerId);

    /**
     * Kiểm tra xem số điện thoại đã được sử dụng chưa
     *
     * @param phoneNumber - Số điện thoại cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByPhoneNumber(String phoneNumber);

    /**
     * Kiểm tra xem email đã được sử dụng chưa
     *
     * @param email - Email cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByEmail(String email);

    /**
     * Kiểm tra xem số CCCD đã được sử dụng chưa
     *
     * @param citizenId - CCCD cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByCitizenId(String citizenId);

    /**
     * Cập nhật thông tin cá nhân của Customer (bởi chính họ)
     * Chỉ cho phép cập nhật một số trường nhất định
     *
     * @param customerId - ID của khách hàng
     * @param requestDTO - Thông tin cập nhật
     * @return CustomerResponseDTO đã được cập nhật
     * @throws ResourceNotFoundException nếu không tìm thấy
     * @throws DuplicateResourceException nếu thông tin mới bị trùng
     */
    CustomerResponseDTO updateCustomerProfile(Long customerId, UpdateCustomerProfileRequestDTO requestDTO);
}
