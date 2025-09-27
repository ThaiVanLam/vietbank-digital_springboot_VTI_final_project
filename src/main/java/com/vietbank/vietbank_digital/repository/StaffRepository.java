package com.vietbank.vietbank_digital.repository;

import com.vietbank.vietbank_digital.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    // Lấy tất cả code, sắp xếp tăng dần theo phần số
    @Query("SELECT s.employeeCode FROM Staff s ORDER BY s.employeeCode ASC")
    List<String> findAllEmployeeCodesOrdered();
}
