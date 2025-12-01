package com.sks.demo.app.gastos.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private  Long id;
    @NotBlank(message = "User cannot be empty")
    private String username;
    //@NotBlank(message = "Password cannot be empty")
    private String password;
    @NotBlank(message = "Full name cannot be empty")
    private String fullName;
    @NotBlank(message = "Position cannot be empty")
    private String position;
    @NotBlank(message = "Department cannot be empty")
    private String department;
    @NotNull(message = "User role cannot be empty")
    private String userRole;
}
