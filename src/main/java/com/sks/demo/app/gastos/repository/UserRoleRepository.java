package com.sks.demo.app.gastos.repository;

import com.sks.demo.app.gastos.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository <UserRole, Long> {
    UserRole findByRole(String role);
    boolean existsByRole(String role);
}
