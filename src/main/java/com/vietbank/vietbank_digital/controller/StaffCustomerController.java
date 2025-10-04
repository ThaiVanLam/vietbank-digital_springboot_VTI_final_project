package com.vietbank.vietbank_digital.controller;

import com.vietbank.vietbank_digital.dto.request.CustomerRequestDTO;
import com.vietbank.vietbank_digital.dto.response.CustomerResponseDTO;
import com.vietbank.vietbank_digital.model.Customer;
import com.vietbank.vietbank_digital.service.CustomerService;
import com.vietbank.vietbank_digital.specification.CustomerSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller cho Staff quản lý khách hàng
 */
@RestController
@RequestMapping("/api/v1/staff/customers")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class StaffCustomerController {
    private final CustomerService customerService;

    /**
     * Tìm kiếm/Liệt kê khách hàng với filter
     * GET /api/v1/staff/customers?fullName=Nguyen&phoneNumber=0123456789&citizenId=123456789
     */
    @GetMapping
    public ResponseEntity<Page<CustomerResponseDTO>> searchCustomers(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String citizenId,
            @RequestParam(required = false) String email,
            @PageableDefault(size = 10, sort = "user.createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        // Build Specification
        Specification<Customer> spec = Specification.where(CustomerSpecification.isActive());

        if (fullName != null && !fullName.trim().isEmpty()) {
            spec = spec.and(CustomerSpecification.hasFullNameContaining(fullName));
        }
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            spec = spec.and(CustomerSpecification.hasPhoneNumber(phoneNumber));
        }
        if (citizenId != null && !citizenId.trim().isEmpty()) {
            spec = spec.and(CustomerSpecification.hasCitizenId(citizenId));
        }
        if (email != null && !email.trim().isEmpty()) {
            spec = spec.and(CustomerSpecification.hasEmail(email));
        }

        Page<CustomerResponseDTO> customers = customerService.searchCustomers(spec, pageable);
        return ResponseEntity.ok(customers);
    }

    /**
     * Xem chi tiết khách hàng
     * GET /api/v1/staff/customers/{customerId}
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long customerId) {
        CustomerResponseDTO customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customer);
    }

    /**
     * Thêm khách hàng mới
     * POST /api/v1/staff/customers
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCustomer(@Valid @RequestBody CustomerRequestDTO requestDTO) {
        CustomerResponseDTO createdCustomer = customerService.createCustomer(requestDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "success.customer.created");
        response.put("data", createdCustomer);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Cập nhật thông tin khách hàng
     * PUT /api/v1/staff/customers/{customerId}
     */
    @PutMapping("/{customerId}")
    public ResponseEntity<Map<String, Object>> updateCustomer(
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerRequestDTO requestDTO) {

        CustomerResponseDTO updatedCustomer = customerService.updateCustomer(customerId, requestDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "success.customer.updated");
        response.put("data", updatedCustomer);

        return ResponseEntity.ok(response);
    }

    /**
     * Xóa mềm khách hàng (cập nhật status thành INACTIVE)
     * DELETE /api/v1/staff/customers/{customerId}
     */
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Map<String, String>> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "success.customer.deleted");

        return ResponseEntity.ok(response);
    }
}
