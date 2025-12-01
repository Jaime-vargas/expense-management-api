package com.sks.demo.app.gastos.service.orchestrators;

import com.sks.demo.app.gastos.dto.ExpenseDTO;
import com.sks.demo.app.gastos.service.AttachmentService;
import com.sks.demo.app.gastos.service.ExpenseService;
import com.sks.demo.app.gastos.service.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseOrchestrator {

    private final ProjectService projectService;
    private final ExpenseService expenseService;
    private final AttachmentService attachmentService;

    public ExpenseOrchestrator(ProjectService projectService, ExpenseService expenseService, AttachmentService attachmentService) {
        this.projectService = projectService;
        this.expenseService = expenseService;
        this.attachmentService = attachmentService;
    }

    //Method to get all expenses and their attachments associated
    public List<ExpenseDTO> getExpensesByProjectIdWithAttachmentsAssociated(Long projectId){
        projectService.findById(projectId);

        List<ExpenseDTO> expensesDTO = expenseService.getExpensesByProjectId(projectId);
        expensesDTO.forEach(expenseDTO -> {
            Long  attachmentsAdded =  attachmentService.countAttachments(expenseDTO.getId());
            expenseDTO.setAssociatedFiles(attachmentsAdded);
        });
        return expensesDTO;
    }

    public ExpenseDTO getExpenseByIdWithAttachmentsAssociated(Long projectId, Long expenseId){
        ExpenseDTO expenseDTO = expenseService.getExpenseById(projectId, expenseId);
        Long  attachmentsAdded =  attachmentService.countAttachments(expenseDTO.getId());
        expenseDTO.setAssociatedFiles(attachmentsAdded);
        return expenseDTO;
    }

    public ExpenseDTO addExpense(Long projectId, ExpenseDTO expenseDTO){
        projectService.findById(projectId);
        expenseDTO = expenseService.addExpense(projectId, expenseDTO);
        long  attachmentsAdded =  attachmentService.countAttachments(expenseDTO.getId());
        expenseDTO.setAssociatedFiles(attachmentsAdded);
        return expenseDTO;
    }

    public ExpenseDTO updateExpense(Long projectId,Long expenseID, ExpenseDTO expenseDTO){
        expenseDTO = expenseService.updateExpense(projectId, expenseID, expenseDTO);
        long  attachmentsAdded =  attachmentService.countAttachments(expenseDTO.getId());
        expenseDTO.setAssociatedFiles(attachmentsAdded);
        return expenseDTO;
    }

    // Method to delete all attachments associated with an expense before deleting the expense itself
    public void deleteExpenseAndItsFiles(Long projectId, Long expenseId){
        attachmentService.getAttachmentsByExpenseId(expenseId).forEach(attachment -> {
            attachmentService.deleteAttachment(expenseId, attachment.getId());
        });
        expenseService.deleteExpense(projectId, expenseId);
    }

}
