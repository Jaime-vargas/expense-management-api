package com.sks.demo.app.gastos.service.orchestrators;

import com.sks.demo.app.gastos.Mapper.Mapper;
import com.sks.demo.app.gastos.dto.ExpenseDTO;
import com.sks.demo.app.gastos.model.Expense;
import com.sks.demo.app.gastos.model.Project;
import com.sks.demo.app.gastos.service.AttachmentService;
import com.sks.demo.app.gastos.service.ExpenseService;
import com.sks.demo.app.gastos.service.ProjectService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseOrchestrator {

    private final ProjectService projectService;
    private final ExpenseService expenseService;
    private final AttachmentService attachmentService;
    private final Mapper mapper;

    // --- CONSTRUCTOR ---
    public ExpenseOrchestrator(ProjectService projectService, ExpenseService expenseService, AttachmentService attachmentService, Mapper mapper) {
        this.projectService = projectService;
        this.expenseService = expenseService;
        this.attachmentService = attachmentService;
        this.mapper = mapper;
    }

    // --- METHODS ---
    public List<ExpenseDTO> getExpensesByProjectIdWithAttachmentsAssociated(Long projectId){
        // VALIDATIONS
        projectService.findById(projectId);
        List<ExpenseDTO> expensesDTO = expenseService.getExpensesByProjectId(projectId).stream().map(mapper::entityToDto).toList();
        // ADD NUMBER OF ATTACHMENTS TO DTO
        expensesDTO.forEach(expenseDTO -> {
            Long attachments = attachmentService.countAttachments(expenseDTO.getId());
            expenseDTO.setAssociatedFiles(attachments);
        });
        return expensesDTO;
    }

    public ExpenseDTO getExpenseByProjectIdWithAttachmentsAssociated(Long projectId, Long expenseId){
        // VALIDATIONS
        projectService.findById(projectId);
        Expense expense = expenseService.getExpenseById(expenseId);
        ExpenseDTO expenseDTO = mapper.entityToDto(expense);
        // ADD NUMBER OF ATTACHMENTS TO DTO
        Long  attachments =  attachmentService.countAttachments(expenseDTO.getId());
        expenseDTO.setAssociatedFiles(attachments);
        return expenseDTO;
    }

    public ExpenseDTO saveExpense(Long projectId, ExpenseDTO expenseDTO){
        // VALIDATIONS
        Project project = projectService.findById(projectId);

        Expense expense = mapper.dtoToEntity(expenseDTO);
        expense.setProject(project);
        expense = expenseService.saveExpense(expense);
        expenseDTO = mapper.entityToDto(expense);
        // ADD NUMBER OF ATTACHMENTS TO DTO
        long  attachmentsAdded =  attachmentService.countAttachments(expenseDTO.getId());
        expenseDTO.setAssociatedFiles(attachmentsAdded);
        return expenseDTO;
    }

    public ExpenseDTO updateExpense(Long projectId,Long expenseID, ExpenseDTO expenseDTO){
        // VALIDATIONS
        Project project = projectService.findById(projectId);
        expenseService.findById(expenseID);

        Expense expense = mapper.dtoToEntity(expenseDTO);
        expense.setProject(project);
        expense.setId(expenseID);
        expense = expenseService.saveExpense(expense);
        expenseDTO = mapper.entityToDto(expense);
        // ADD NUMBER OF ATTACHMENTS TO DTO
        long  attachmentsAdded =  attachmentService.countAttachments(expenseDTO.getId());
        expenseDTO.setAssociatedFiles(attachmentsAdded);
        return expenseDTO;
    }

    // DELETE ALL ATTACHMENTS BEFORE DELETE AN EXPENSE
    @Transactional
    public void deleteExpenseAndItsFiles(Long projectId, Long expenseId){
        // VALIDATIONS
        projectService.findById(projectId);
        expenseService.findById(expenseId);

        attachmentService.getAttachmentsByExpenseId(expenseId).forEach(
                attachment -> attachmentService.deleteAttachment(expenseId, attachment.getId()));
        expenseService.deleteExpense(expenseId);
    }
}
