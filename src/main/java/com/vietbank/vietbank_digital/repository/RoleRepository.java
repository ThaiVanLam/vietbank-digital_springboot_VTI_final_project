package com.vietbank.vietbank_digital.repository;

import com.vietbank.vietbank_digital.model.AppRole;
import com.vietbank.vietbank_digital.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByRoleName(AppRole appRole);
}
