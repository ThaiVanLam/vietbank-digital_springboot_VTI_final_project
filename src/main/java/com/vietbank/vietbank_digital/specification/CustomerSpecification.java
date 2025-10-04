package com.vietbank.vietbank_digital.specification;

import com.vietbank.vietbank_digital.model.Customer;
import com.vietbank.vietbank_digital.model.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification để lọc Customer theo các tiêu chí
 * Sử dụng cho tìm kiếm khách hàng bởi Staff
 */
public class CustomerSpecification {
    /**
     * Tìm kiếm theo tên (không phân biệt hoa thường, chứa chuỗi)
     */
    public static Specification<Customer> hasFullNameContaining(String fullName) {
        return (root, query, criteriaBuilder) -> {
            if (fullName == null || fullName.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Customer, User> userJoin = root.join("user");
            return criteriaBuilder.like(
                    criteriaBuilder.lower(userJoin.get("fullName")),
                    "%" + fullName.toLowerCase() + "%"
            );
        };
    }

    /**
     * Tìm kiếm theo số điện thoại
     */
    public static Specification<Customer> hasPhoneNumber(String phoneNumber) {
        return (root, query, criteriaBuilder) -> {
            if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Customer, User> userJoin = root.join("user");
            return criteriaBuilder.equal(userJoin.get("phoneNumber"), phoneNumber);
        };
    }

    /**
     * Tìm kiếm theo số CCCD
     */
    public static Specification<Customer> hasCitizenId(String citizenId) {
        return (root, query, criteriaBuilder) -> {
            if (citizenId == null || citizenId.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("citizenId"), citizenId);
        };
    }

    /**
     * Chỉ lấy khách hàng chưa bị xóa (status != INACTIVE)
     */
    public static Specification<Customer> isActive() {
        return (root, query, criteriaBuilder) -> {
            Join<Customer, User> userJoin = root.join("user");
            return criteriaBuilder.notEqual(userJoin.get("status"), User.Status.INACTIVE);
        };
    }

    /**
     * Tìm theo email
     */
    public static Specification<Customer> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<Customer, User> userJoin = root.join("user");
            return criteriaBuilder.equal(
                    criteriaBuilder.lower(userJoin.get("email")),
                    email.toLowerCase()
            );
        };
    }
}
