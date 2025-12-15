package com.sks.demo.app.gastos.Mapper;

import com.sks.demo.app.gastos.dto.AttachmentDTO;
import com.sks.demo.app.gastos.dto.ExpenseDTO;
import com.sks.demo.app.gastos.dto.ProjectDTO;
import com.sks.demo.app.gastos.model.Expense;
import com.sks.demo.app.gastos.model.Project;
import com.sks.demo.app.gastos.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    // --- ATTACHMENTS ---
    //public Attachment e
    // --- EXPENSES ---
    public Expense dtoToEntity(ExpenseDTO expenseDTO) {
        Expense expense = new Expense();
        expense.setAmount(expenseDTO.getAmount());
        expense.setBill(expenseDTO.getBill());
        expense.setSupplier(expenseDTO.getSupplier());
        expense.setDescription(expenseDTO.getDescription());
        expense.setDate(expenseDTO.getDate());
        return expense;
    }

    public ExpenseDTO entityToDto(Expense expense) {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setId(expense.getId());
        expenseDTO.setAmount(expense.getAmount());
        expenseDTO.setBill(expense.getBill());
        expenseDTO.setSupplier(expense.getSupplier());
        expenseDTO.setDescription(expense.getDescription());
        expenseDTO.setDate(expense.getDate());
        return expenseDTO;
    }

    // --- PROJECTS ---
    public Project dtoToEntity (ProjectDTO projectDTO, UserEntity userEntity) {
        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setBudget(projectDTO.getBudget());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setUser(userEntity);
        return project;
    }

    public ProjectDTO entityToDto (Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setName(project.getName());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setBudget(project.getBudget());
        projectDTO.setStartDate(project.getStartDate());
        projectDTO.setEndDate(project.getEndDate());
        projectDTO.setUsername(project.getUser().getUsername());
        return projectDTO;
    }


}
