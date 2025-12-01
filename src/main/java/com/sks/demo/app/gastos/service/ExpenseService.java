package com.sks.demo.app.gastos.service;

import com.sks.demo.app.gastos.dto.ExpenseDTO;
import com.sks.demo.app.gastos.exeptionsHandler.HandleException;
import com.sks.demo.app.gastos.model.Expense;
import com.sks.demo.app.gastos.model.Project;
import com.sks.demo.app.gastos.service.orchestrators.ProjectOrchestrator;
import com.sks.demo.app.gastos.repository.ExpenseRepository;
import jakarta.transaction.Transactional;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ProjectService projectService;
    private final ProjectOrchestrator projectOrchestrator;

    // Pendiente desacoplamiento organización de
    // clases repositorio y servicio

    public ExpenseService(ExpenseRepository expenseRepository, ProjectService projectService, @Lazy ProjectOrchestrator projectOrchestrator) {
        this.expenseRepository = expenseRepository;
        this.projectService = projectService;
        this.projectOrchestrator = projectOrchestrator;
    }

    // Transactional methods

    public List<ExpenseDTO> getExpensesByProjectId(Long projectId) {
        List<Expense> expenses = expenseRepository.findByProjectId(projectId);
        List<ExpenseDTO> expenseDTOS = new ArrayList<>();
        expenses.forEach(expense -> {
            expenseDTOS.add(entityToDto(expense));
        });
        return expenseDTOS;
    }

    public ExpenseDTO getExpenseById(Long projectId, Long expenseId) {
        Expense expense = findByIdAndProjectId(expenseId, projectId);
        return entityToDto(expense);
    }

    @Transactional
    public ExpenseDTO addExpense(Long projectId, ExpenseDTO expenseDTO) {
        projectService.findById(projectId);
        expenseDTO.setId(null);
        Expense expense = dtoToEntity(projectId, expenseDTO);
        expense = expenseRepository.save(expense);
        expenseDTO = entityToDto(expense);
        return expenseDTO;
    }

    @Transactional
    public ExpenseDTO updateExpense(Long projectId, Long expenseId, ExpenseDTO expenseDTO){
        findByIdAndProjectId(expenseId, projectId);
        expenseDTO.setId(expenseId);
        Expense expense = dtoToEntity(projectId, expenseDTO);
        expense = expenseRepository.save(expense);
        expenseDTO = entityToDto(expense);
        return expenseDTO;
    }

    @Transactional
    public void deleteExpense(Long projectId, Long expenseId){
        findByIdAndProjectId(expenseId, projectId);
        expenseRepository.deleteById(expenseId);
    }

    // No transactional methods

    public Expense findById(Long expenseId){
        return expenseRepository.findById(expenseId).orElseThrow(
                () -> new HandleException(400, "Bad Request", "Expense with id: " + expenseId + " does not exist")
        );
    }

    public Expense findByIdAndProjectId(Long expenseId, Long projectId){
        return expenseRepository.findByIdAndProjectId(expenseId, projectId).orElseThrow(
                () -> new HandleException(400, "Bad Request", "Expense with id: " + expenseId + " does not exist in project with id: " + projectId)
        );
    }

    public Long getProjectIdByExpenseId(Long expenseId) {
        Expense expense = findById(expenseId);
        return expense.getProject().getId();
    }

    public List<Expense> getExpenses(Long projectId) {
        return expenseRepository.findByProjectId(projectId);
    }
    //This method are used to convert between Entity to DTO
     // Do not add the associations from attachments here to avoid circular dependencies
    //
    private ExpenseDTO entityToDto(Expense expense){
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setId(expense.getId());
        expenseDTO.setAmount(expense.getAmount());
        expenseDTO.setBill(expense.getBill());
        expenseDTO.setSupplier(expense.getSupplier());
        expenseDTO.setDescription(expense.getDescription());
        expenseDTO.setDate(expense.getDate());
        return expenseDTO;
    }

    private Expense dtoToEntity(Long projectId,ExpenseDTO expenseDTO){
        Expense expense = new Expense();
        expense.setId(expenseDTO.getId());
        expense.setAmount(expenseDTO.getAmount());
        expense.setBill(expenseDTO.getBill());
        expense.setSupplier(expenseDTO.getSupplier());
        expense.setDescription(expenseDTO.getDescription());
        expense.setDate(expenseDTO.getDate());
        Project project = projectOrchestrator.dtoToEntity(projectService.getProject(projectId));
        expense.setProject(project);
        return expense;
    }

}
