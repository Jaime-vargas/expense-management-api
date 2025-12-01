package com.sks.demo.app.gastos.service;

import com.sks.demo.app.gastos.dto.UserDTO;

import com.sks.demo.app.gastos.exeptionsHandler.HandleException;
import com.sks.demo.app.gastos.model.UserEntity;
import com.sks.demo.app.gastos.model.UserRole;
import com.sks.demo.app.gastos.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserEntityService {


    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;

    public UserEntityService(UserRepository userRepository, UserRoleService userRoleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream().map(this::entityToDTO).toList();
    }

    public UserDTO getUser(Long userId) {
        UserEntity userEntity = findById(userId);
        return entityToDTO(userEntity);
    }

    @Transactional
    public UserDTO addUser(UserDTO userDTO) {
        // Check if duplicated
        if(userRepository.existsByUsername(userDTO.getUsername())){
            throw new HandleException(409, "Conflict", "Username already exists");
        }
        userDTO.setId(null);
        // corregir y optimizar
        UserEntity userEntity = new UserEntity();
        userEntity = dtoToEntity(userDTO, userEntity);
        userEntity = userRepository.save(userEntity);
        userDTO = entityToDTO(userEntity);
        return userDTO;
    }

    @Transactional
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        if(userId == 1){
            throw new HandleException(409, "Conflict", "username root could not be edited");
        }

        UserEntity userEntity = findById(userId);

        if(userRepository.existsByUsernameAndIdNot(userDTO.getUsername(), userEntity.getId())){
            throw new HandleException(409, "Conflict", "Username already exists");
        }
        UserEntity existingUserEntity = findById(userId);

        userEntity = dtoToEntity(userDTO, existingUserEntity);
        userEntity = userRepository.save(userEntity);
        userDTO = entityToDTO(userEntity);
        return userDTO;
    }

    @Transactional
    public void deleteUser(Long userId) {
        findById(userId);
        userRepository.deleteById(userId);
    }

    // No transactional methods
    public UserEntity findById(Long userId){
        return userRepository.findById(userId).orElseThrow(
                () -> new HandleException(400, "Bad Request", "ID does not exist")
        );
    }

    public void isUserRoot (Long userId) {
        UserEntity userEntity = findById(userId);
        if(userEntity.getUsername().equals("root")){
            throw new HandleException(409, "Conflict", "username root could not be deleted");
        }
    }

    // Not transactional methods
    private UserEntity dtoToEntity (UserDTO userDTO, UserEntity userEntity) {

        userEntity.setId(userDTO.getId());
        userEntity.setUsername(userDTO.getUsername());

        // Password encrypted with Bcrypt, password encoder
        if (userDTO.getPassword() != null) {
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        //
        System.out.println("userEntity password: " + userEntity.getPassword());
        System.out.println("userDTO password: " + userDTO.getPassword());
        userEntity.setFullName(userDTO.getFullName());
        userEntity.setPosition(userDTO.getPosition());
        userEntity.setDepartment(userDTO.getDepartment());

        // Create a userRole entity / in constructor generates Trim and toLowerCase
        UserRole userRole = new UserRole(userDTO.getUserRole());
        userRoleService.existsByRole(userRole);
        // find and return with id
        userRole = userRoleService.findByRole(userRole.getRole());
        userEntity.setUserRole(userRole);

        return userEntity;
    }

    private UserDTO entityToDTO (UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setUsername(userEntity.getUsername());
        userDTO.setFullName(userEntity.getFullName());
        userDTO.setPosition(userEntity.getPosition());
        userDTO.setDepartment(userEntity.getDepartment());
        userDTO.setUserRole(userEntity.getUserRole().getRole());
        return userDTO;
    }

    public UserEntity findByUsername(String username) {
        UserEntity userEntity;
        return userEntity = userRepository.findByUsername(username).orElseThrow(() ->
                new HandleException(400, "Bad Request", "Username not found"));
    }
}
