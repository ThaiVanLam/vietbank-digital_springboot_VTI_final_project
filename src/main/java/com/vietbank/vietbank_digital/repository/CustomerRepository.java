package com.vietbank.vietbank_digital.repository;

import com.vietbank.vietbank_digital.model.Customer;
import com.vietbank.vietbank_digital.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository cho Customer Entity
 * Hỗ trợ JPA Specification để tìm kiếm động
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    /**
     * Tìm Customer theo số CCCD
     *
     * @param citizenId - Số căn cước công dân
     * @return Optional<Customer>
     */
    Optional<Customer> findByCitizenId(String citizenId);

    /**
     * Kiểm tra CCCD đã tồn tại chưa
     *
     * @param citizenId - Số CCCD cần kiểm tra
     * @return true nếu tồn tại, false nếu không
     */
    boolean existsByCitizenId(String citizenId);

    /**
     * Tìm Customer theo User
     * Hữu ích khi có User object từ Authentication
     *
     * @param user - User entity
     * @return Optional<Customer>
     */
    Optional<Customer> findByUser(User user);

    /**
     * Tìm Customer theo User ID
     *
     * @param userId - ID của User
     * @return Optional<Customer>
     */
    Optional<Customer> findByUserUserId(Long userId);
}