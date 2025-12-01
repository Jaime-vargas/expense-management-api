package com.sks.demo.app.gastos.dto;

import com.sks.demo.app.gastos.enums.ExpenseCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor

@Valid
public class ExpenseDTO {
    private Long id;
    @NotNull(message = "Amount cannot be empty")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    private String bill;
    @NotNull(message = "Category cannot be empty")
    private String supplier;
    @NotNull(message = "Description cannot be empty")
    @NotEmpty(message = "Description cannot be empty")
    private String description;
    @NotNull(message = "Date cannot be empty")
    private LocalDate date;
    private Long associatedFiles;
}
