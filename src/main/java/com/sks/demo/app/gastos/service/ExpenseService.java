package com.sks.demo.app.gastos.service;

import com.sks.demo.app.gastos.exeptionsHandler.HandleException;
import com.sks.demo.app.gastos.model.Expense;
import com.sks.demo.app.gastos.repository.ExpenseRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    // --- CONSTRUCTOR ---
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // --- METHODS ---
    public List<Expense> getExpensesByProjectId(Long projectId) {
        return  expenseRepository.findAllByProjectId(projectId);
    }

    public Expense getExpenseById(Long expenseId) {
        return findById(expenseId);
    }

    @Transactional
    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Transactional
    public void deleteExpense(Long expenseId){
        expenseRepository.deleteById(expenseId);
    }

    // VALIDATIONS
    public Expense findById(Long expenseId){
        return expenseRepository.findById(expenseId).orElseThrow(
                () -> new HandleException(400, "Bad Request", "Expense with id: " + expenseId + " does not exist")
        );
    }

    /** REVIEWING */
    public Long getProjectIdByExpenseId(Long expenseId) {
        Expense expense = findById(expenseId);
        return expense.getProject().getId();
    }
}
