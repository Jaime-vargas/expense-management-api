package com.sks.demo.app.gastos.repository;

import com.sks.demo.app.gastos.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByProjectId(Long projectId);
    @NonNull
    Optional<Expense> findById(@NonNull Long expenseId);
}
