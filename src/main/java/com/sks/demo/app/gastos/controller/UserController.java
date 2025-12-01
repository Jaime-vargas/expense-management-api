package com.sks.demo.app.gastos.controller;

import com.sks.demo.app.gastos.dto.UserDTO;
import com.sks.demo.app.gastos.service.orchestrators.UserOrchestrator;
import com.sks.demo.app.gastos.service.UserEntityService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserEntityService userEntityService;
    private final UserOrchestrator userOrchestrator;

    public UserController(UserEntityService userEntityService, UserOrchestrator userOrchestrator) {
        this.userEntityService = userEntityService;
        this.userOrchestrator = userOrchestrator;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<?>> getUsers() {
        List<UserDTO> users = userEntityService.getAllUsers();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        UserDTO user = userEntityService.getUser(userId);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody UserDTO userDTO) {
        userDTO = userEntityService.addUser(userDTO);
        return ResponseEntity.ok().body(userDTO);
    }

    @PutMapping("{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long userId, @Valid @RequestBody UserDTO userDTO) {
        userDTO = userEntityService.updateUser(userId, userDTO);
        return ResponseEntity.ok().body(userDTO);
    }


    @DeleteMapping("/{userID}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userID) {
        userOrchestrator.deleteUser(userID);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "User with id " + userID + " has been deleted");
        return ResponseEntity.ok().body(response.toString());
    }
}
