package com.sks.demo.app.gastos.repository;

import com.sks.demo.app.gastos.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findByProject_Id(Long projectId);
    List<Expense> findByProjectId(Long projectId);


    // REVIEWED METHODS
    @NonNull
    Optional<Expense> findById(@NonNull Long expenseId);
    Optional<Expense> findByIdAndProjectId(Long expenseId, Long projectId);

}
