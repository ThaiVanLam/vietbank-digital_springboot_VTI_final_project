package com.vietbank.vietbank_digital.service;

import com.vietbank.vietbank_digital.config.exception.DuplicateResourceException;
import com.vietbank.vietbank_digital.config.exception.ResourceNotFoundException;
import com.vietbank.vietbank_digital.dto.request.CustomerRequestDTO;
import com.vietbank.vietbank_digital.dto.request.UpdateCustomerProfileRequestDTO;
import com.vietbank.vietbank_digital.dto.response.CustomerResponseDTO;
import com.vietbank.vietbank_digital.model.AppRole;
import com.vietbank.vietbank_digital.model.Customer;
import com.vietbank.vietbank_digital.model.Role;
import com.vietbank.vietbank_digital.model.User;
import com.vietbank.vietbank_digital.repository.CustomerRepository;
import com.vietbank.vietbank_digital.repository.RoleRepository;
import com.vietbank.vietbank_digital.repository.UserRepository;
import com.vietbank.vietbank_digital.request.CreatingCustomerRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation của CustomerService
 * Xử lý tất cả business logic liên quan đến Customer
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    /**
     * Tìm kiếm khách hàng với Specification
     */
    @Override
    public Page<CustomerResponseDTO> searchCustomers(Specification<Customer> spec, Pageable pageable) {
        Page<Customer> customers = customerRepository.findAll(spec, pageable);
        return customers.map(this::convertToResponseDTO);
    }

    /**
     * Lấy chi tiết khách hàng theo ID
     */
    @Override
    public CustomerResponseDTO getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("error.customer.notFound", customerId));

        return convertToResponseDTO(customer);
    }

    /**
     * Lấy customer theo User ID
     */
    @Override
    public CustomerResponseDTO getCustomerByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("error.user.notFound", userId));

        Customer customer = customerRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("error.customer.notFound.byUser", userId));

        return convertToResponseDTO(customer);
    }

    /**
     * Tạo khách hàng mới (bởi Staff)
     */
    @Override
    @Transactional
    public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) {
        // Kiểm tra trùng lặp
        validateDuplicateData(requestDTO);

        // Tạo User
        User user = new User(
                requestDTO.getUsername(),
                requestDTO.getPhoneNumber(),
                requestDTO.getFullName(),
                requestDTO.getEmail(),
                passwordEncoder.encode("Abc@123456") // Mật khẩu mặc định
        );

        // Gán role CUSTOMER
        Role customerRole = roleRepository.findByRoleName(AppRole.ROLE_CUSTOMER)
                .orElseThrow(() -> new RuntimeException("Error: Role CUSTOMER not found!"));
        user.setRole(customerRole);

        // Tạo Customer
        Customer customer = new Customer(
                requestDTO.getCitizenId(),
                requestDTO.getAddress(),
                requestDTO.getDateOfBirth(),
                requestDTO.getGender(),
                requestDTO.getOccupation()
        );
        customer.setUser(user);

        Customer savedCustomer = customerRepository.save(customer);
        return convertToResponseDTO(savedCustomer);
    }

    /**
     * Cập nhật thông tin khách hàng
     */
    @Override
    @Transactional
    public CustomerResponseDTO updateCustomer(Long customerId, CustomerRequestDTO requestDTO) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("error.customer.notFound", customerId));

        // Kiểm tra trùng lặp (ngoại trừ chính nó)
        validateDuplicateDataForUpdate(customerId, requestDTO);

        // Cập nhật User
        User user = customer.getUser();
        user.setFullName(requestDTO.getFullName());
        user.setEmail(requestDTO.getEmail());
        user.setPhoneNumber(requestDTO.getPhoneNumber());

        // Cập nhật Customer
        customer.setCitizenId(requestDTO.getCitizenId());
        customer.setAddress(requestDTO.getAddress());
        customer.setDateOfBirth(requestDTO.getDateOfBirth());
        customer.setGender(requestDTO.getGender());
        customer.setOccupation(requestDTO.getOccupation());

        Customer updatedCustomer = customerRepository.save(customer);
        return convertToResponseDTO(updatedCustomer);
    }

    /**
     * Xóa mềm khách hàng (cập nhật status thành INACTIVE)
     */
    @Override
    @Transactional
    public void deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("error.customer.notFound", customerId));

        User user = customer.getUser();
        user.setStatus(User.Status.INACTIVE);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public CustomerResponseDTO updateCustomerProfile(Long customerId, UpdateCustomerProfileRequestDTO requestDTO) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("error.customer.notFound", customerId));

        // Kiểm tra trùng lặp phone và email (ngoại trừ chính nó)
        validateProfileUpdateData(customerId, requestDTO);

        // Cập nhật User info
        User user = customer.getUser();
        user.setFullName(requestDTO.getFullName());
        user.setEmail(requestDTO.getEmail());
        user.setPhoneNumber(requestDTO.getPhoneNumber());

        // Cập nhật Customer info (chỉ cho phép cập nhật address và occupation)
        customer.setAddress(requestDTO.getAddress());
        customer.setOccupation(requestDTO.getOccupation());

        Customer updatedCustomer = customerRepository.save(customer);
        return convertToResponseDTO(updatedCustomer);
    }

    /**
     * Validate dữ liệu khi customer tự cập nhật profile
     */
    private void validateProfileUpdateData(Long customerId, UpdateCustomerProfileRequestDTO requestDTO) {
        // Kiểm tra phone number
        userRepository.findByPhoneNumber(requestDTO.getPhoneNumber())
                .ifPresent(user -> {
                    Customer existingCustomer = customerRepository.findByUser(user).orElse(null);
                    if (existingCustomer != null && !existingCustomer.getCustomerId().equals(customerId)) {
                        throw new DuplicateResourceException("error.customer.duplicatePhone");
                    }
                });

        // Kiểm tra email
        userRepository.findByEmail(requestDTO.getEmail())
                .ifPresent(user -> {
                    Customer existingCustomer = customerRepository.findByUser(user).orElse(null);
                    if (existingCustomer != null && !existingCustomer.getCustomerId().equals(customerId)) {
                        throw new DuplicateResourceException("error.customer.duplicateEmail");
                    }
                });
    }

    /**
     * Kiểm tra Customer tồn tại
     */
    @Override
    public boolean existsById(Long customerId) {
        return customerRepository.existsById(customerId);
    }

    /**
     * Kiểm tra số điện thoại đã tồn tại
     */
    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    /**
     * Kiểm tra email đã tồn tại
     */
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Kiểm tra CCCD đã tồn tại
     */
    @Override
    public boolean existsByCitizenId(String citizenId) {
        return customerRepository.existsByCitizenId(citizenId);
    }

    // ==================== PRIVATE METHODS ====================

    /**
     * Validate dữ liệu trùng lặp khi tạo mới
     */
    private void validateDuplicateData(CustomerRequestDTO requestDTO) {
        if (userRepository.existsByPhoneNumber(requestDTO.getPhoneNumber())) {
            throw new DuplicateResourceException("error.customer.duplicatePhone");
        }
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new DuplicateResourceException("error.customer.duplicateEmail");
        }
        if (customerRepository.existsByCitizenId(requestDTO.getCitizenId())) {
            throw new DuplicateResourceException("error.customer.duplicateCitizenId");
        }
        if (userRepository.existsByUsername(requestDTO.getUsername())) {
            throw new DuplicateResourceException("error.customer.duplicateUsername");
        }
    }

    /**
     * Validate dữ liệu trùng lặp khi cập nhật
     */
    private void validateDuplicateDataForUpdate(Long customerId, CustomerRequestDTO requestDTO) {
        // Kiểm tra phone number (ngoại trừ customer hiện tại)
        userRepository.findByPhoneNumber(requestDTO.getPhoneNumber())
                .ifPresent(user -> {
                    Customer existingCustomer = customerRepository.findByUser(user).orElse(null);
                    if (existingCustomer != null && !existingCustomer.getCustomerId().equals(customerId)) {
                        throw new DuplicateResourceException("error.customer.duplicatePhone");
                    }
                });

        // Kiểm tra email
        userRepository.findByEmail(requestDTO.getEmail())
                .ifPresent(user -> {
                    Customer existingCustomer = customerRepository.findByUser(user).orElse(null);
                    if (existingCustomer != null && !existingCustomer.getCustomerId().equals(customerId)) {
                        throw new DuplicateResourceException("error.customer.duplicateEmail");
                    }
                });

        // Kiểm tra citizen ID
        customerRepository.findByCitizenId(requestDTO.getCitizenId())
                .ifPresent(customer -> {
                    if (!customer.getCustomerId().equals(customerId)) {
                        throw new DuplicateResourceException("error.customer.duplicateCitizenId");
                    }
                });
    }

    /**
     * Convert Customer entity to ResponseDTO
     */
    private CustomerResponseDTO convertToResponseDTO(Customer customer) {
        CustomerResponseDTO dto = new CustomerResponseDTO();
        dto.setCustomerId(customer.getCustomerId());
        dto.setCitizenId(customer.getCitizenId());
        dto.setAddress(customer.getAddress());
        dto.setDateOfBirth(customer.getDateOfBirth());
        dto.setGender(customer.getGender());
        dto.setOccupation(customer.getOccupation());

        // User info
        User user = customer.getUser();
        dto.setFullName(user.getFullName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());

        // Total accounts
        dto.setTotalAccounts(customer.getBankAccounts() != null ? customer.getBankAccounts().size() : 0);

        return dto;
    }
}
