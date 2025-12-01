package com.sks.demo.app.gastos.service;

import com.sks.demo.app.gastos.exeptionsHandler.HandleException;
import com.sks.demo.app.gastos.model.UserRole;
import com.sks.demo.app.gastos.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public List<UserRole> getUserRoles() {
        return userRoleRepository.findAll();
    }

    @Transactional
    public UserRole addUserRole(UserRole userRole) {
        String role = normalizeRole(userRole);
        if(userRoleRepository.existsByRole(role)) {
            throw new HandleException(409, "Conflict", "The role with name " + role + " already exists");
        }
        userRole.setRole(role);
        return userRoleRepository.save(userRole);
    }

    @Transactional
    public void deleteUserRole(UserRole userRole) {
        String role = normalizeRole(userRole);
        if (role.equals("admin") || role.equals("user") ) {
            throw new HandleException(400, "Bad Request", "Admin or User role couldn't be deleted");
        }
        existsByRole(userRole);
        UserRole roleToDelete = userRoleRepository.findByRole(role);
        userRoleRepository.deleteById(roleToDelete.getId());

    }

    // Not transactional methods
    private String normalizeRole (UserRole userRole) {
        return userRole.getRole().trim().toUpperCase();
    }

    public UserRole findByRole(String role) {
        return userRoleRepository.findByRole(role);
    }

    public void existsByRole (UserRole userRole) {
        String role = normalizeRole(userRole);
        if (!userRoleRepository.existsByRole(role)){
            throw new HandleException(400, "Bad Request", "The role with name " + userRole.getRole() + " does not exist");
        }
    }

}
