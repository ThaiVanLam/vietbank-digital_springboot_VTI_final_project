package com.vietbank.vietbank_digital.service;

import com.vietbank.vietbank_digital.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeCodeGenerator {
    private static final String PREFIX = "EMP";

    @Autowired
    private StaffRepository staffRepository;

    /**
     * Generate code dạng EMPxxx, lấy số nhỏ nhất chưa tồn tại
     */
    @Transactional
    public String generateSequentialUniqueCode() {
        // Lấy toàn bộ code đã có, sắp xếp theo số
        List<String> codes = staffRepository.findAllEmployeeCodesOrdered();

        int expected = 1; // bắt đầu từ EMP001

        for (String code : codes) {
            if (code != null && code.startsWith(PREFIX)) {
                String numberPart = code.substring(PREFIX.length());
                try {
                    int num = Integer.parseInt(numberPart);
                    if (num == expected) {
                        expected++; // đã tồn tại, nhảy sang số tiếp theo
                    } else if (num > expected) {
                        break; // tìm thấy khoảng trống
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        return String.format("%s%03d", PREFIX, expected);
    }
}
