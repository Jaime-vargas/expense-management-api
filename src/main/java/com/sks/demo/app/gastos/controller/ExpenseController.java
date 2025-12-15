package com.sks.demo.app.gastos.controller;

import com.sks.demo.app.gastos.dto.ExpenseDTO;
import com.sks.demo.app.gastos.service.orchestrators.ExpenseOrchestrator;
import com.sks.demo.app.gastos.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ExpenseController {

    private final ExpenseOrchestrator expenseOrchestrator;

    // --- CONSTRUCTOR ---
    public ExpenseController(ExpenseService expenseService, ExpenseOrchestrator expenseOrchestrator) {
        this.expenseOrchestrator = expenseOrchestrator;
    }

    // --- METHODS ---
    @GetMapping("/{projectId}/expenses")
    public ResponseEntity<List<ExpenseDTO>> getExpensesByProjectId(@PathVariable Long projectId) {
        List<ExpenseDTO> expensesDTO = expenseOrchestrator.getExpensesByProjectIdWithAttachmentsAssociated(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(expensesDTO);
    }

    @GetMapping("/{projectId}/expenses/{expenseId}")
    public ResponseEntity<ExpenseDTO> getExpenseByProjectIdAndId(@PathVariable Long projectId, @PathVariable Long expenseId) {
        ExpenseDTO expenseDTO = expenseOrchestrator.getExpenseByProjectIdWithAttachmentsAssociated(projectId, expenseId);
        return ResponseEntity.status(HttpStatus.OK).body(expenseDTO);
    }

    @PostMapping("/{projectId}/expenses")
    public ResponseEntity<ExpenseDTO> saveExpense(@PathVariable Long projectId, @Valid @RequestBody ExpenseDTO expenseDTO) {
        expenseDTO = expenseOrchestrator.saveExpense(projectId, expenseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseDTO);
    }

    @PutMapping("/{projectId}/expenses/{expenseId}")
    public ResponseEntity<ExpenseDTO> updateExpense(@PathVariable Long projectId, @PathVariable Long expenseId, @Valid @RequestBody ExpenseDTO expenseDTO) {
        expenseDTO = expenseOrchestrator.updateExpense(projectId, expenseId, expenseDTO);
        return ResponseEntity.status(HttpStatus.OK).body(expenseDTO);
    }

    @DeleteMapping("/{projectId}/expenses/{expenseId}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long projectId, @PathVariable Long expenseId) {
        expenseOrchestrator.deleteExpenseAndItsFiles(projectId, expenseId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Expense with id " + expenseId + " has been deleted"));
    }
}
