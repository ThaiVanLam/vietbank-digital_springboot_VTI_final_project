package com.vietbank.vietbank_digital.specification;

import com.vietbank.vietbank_digital.model.BankAccount;
import com.vietbank.vietbank_digital.model.Customer;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specification để lọc BankAccount theo các tiêu chí
 */
public class BankAccountSpecification {
    /**
     * Tìm kiếm theo customer ID
     */
    public static Specification<BankAccount> hasCustomerId(Long customerId) {
        return (root, query, criteriaBuilder) -> {
            if (customerId == null) {
                return criteriaBuilder.conjunction();
            }
            Join<BankAccount, Customer> customerJoin = root.join("customer");
            return criteriaBuilder.equal(customerJoin.get("customerId"), customerId);
        };
    }

    /**
     * Tìm kiếm theo số tài khoản
     */
    public static Specification<BankAccount> hasAccountNumber(String accountNumber) {
        return (root, query, criteriaBuilder) -> {
            if (accountNumber == null || accountNumber.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("accountNumber"), accountNumber);
        };
    }

    /**
     * Tìm kiếm theo loại tài khoản
     */
    public static Specification<BankAccount> hasAccountType(BankAccount.AccountType accountType) {
        return (root, query, criteriaBuilder) -> {
            if (accountType == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("accountType"), accountType);
        };
    }

    /**
     * Tìm kiếm theo trạng thái
     */
    public static Specification<BankAccount> hasStatus(BankAccount.Status status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    /**
     * Chỉ lấy tài khoản đang hoạt động
     */
    public static Specification<BankAccount> isActive() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), BankAccount.Status.ACTIVE);
    }

    /**
     * Tìm kiếm theo tên khách hàng
     */
    public static Specification<BankAccount> hasCustomerNameContaining(String customerName) {
        return (root, query, criteriaBuilder) -> {
            if (customerName == null || customerName.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<BankAccount, Customer> customerJoin = root.join("customer");
            return criteriaBuilder.like(
                    criteriaBuilder.lower(customerJoin.get("user").get("fullName")),
                    "%" + customerName.toLowerCase() + "%"
            );
        };
    }
}
