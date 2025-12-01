package com.sks.demo.app.gastos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor


@Valid
public class ProjectDTO {

    Long id;
    @NotEmpty(message = "Name cannot be empty")
    private  String name;

    @NotEmpty(message = "Description cannot be empty")
    private  String description;

    @NotNull(message = "The budget cannot be empty")
    @Positive(message = "The budget must be positive")
    @Min(value = 1, message = "Budget must be at least 1")
    private BigDecimal budget;
    
    @NotNull(message = "Start date cannot be empty")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be empty")
    private LocalDate endDate;

    @NotEmpty(message = "username cannot be empty")
    private String username;
}
