package com.sks.demo.app.gastos.model;

import com.sks.demo.app.gastos.enums.ExpenseCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    private String bill;
    private String supplier;
    private String description;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

}
