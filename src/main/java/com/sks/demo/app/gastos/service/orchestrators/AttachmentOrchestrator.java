package com.sks.demo.app.gastos.service.orchestrators;

import com.sks.demo.app.gastos.dto.AttachmentDTO;
import com.sks.demo.app.gastos.exeptionsHandler.HandleException;
import com.sks.demo.app.gastos.model.Attachment;

import com.sks.demo.app.gastos.service.AttachmentService;
import com.sks.demo.app.gastos.service.ExpenseService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttachmentOrchestrator {

    private final AttachmentService attachmentService;
    private final ExpenseService expenseService;

    public AttachmentOrchestrator(AttachmentService attachmentService, ExpenseService expenseService) {
        this.attachmentService = attachmentService;
        this.expenseService = expenseService;
    }

    public List<AttachmentDTO> getAttachmentsByExpenseId(Long expenseId) {
        expenseService.findById(expenseId);
        return attachmentService.getAttachmentsByExpenseId(expenseId);
    }

    public List<AttachmentDTO> addAttachment(Long expenseId, List<MultipartFile> files) {
        List<AttachmentDTO> attachmentsDtoList = new ArrayList<>();
        expenseService.findById(expenseId);
        Path savePath = getSavePath(expenseId);
        createDirectoriesIfNotExist(savePath);
        files.forEach(file -> {
            Path storePath = createFileStorePath(savePath, file.getOriginalFilename());
            saveFileToPath(file, storePath);
            Attachment attachment = createAttachmentEntity(expenseId, file, storePath);
            attachmentsDtoList.add(attachmentService.addAttachment(attachment));
        });

        return attachmentsDtoList;
    }

    public Path getSavePath(Long expenseId) {
        expenseService.findById(expenseId);
        expenseService.getProjectIdByExpenseId(expenseId);

        String projectDirName = "Proyect_" + expenseService.getProjectIdByExpenseId(expenseId);
        String projectExpenseDesc = "Expense_" + expenseId;
        Path uploadDir = attachmentService.getUploadDir();
        return uploadDir.resolve("attachments/" + projectDirName + "/" + projectExpenseDesc);
    }

    public void createDirectoriesIfNotExist(Path savePath){
        try {
            Files.createDirectories(savePath);
        }catch (IOException e) {
            throw new HandleException(500, "DirectoryCreationError", "Could not create directory for saving attachment files.");
        }
    }

    public Path createFileStorePath(Path savePath, String filename) {
        return savePath.resolve(filename);
    }

    public void saveFileToPath(MultipartFile file, Path storePath) {
        if (Files.exists(storePath)) {
            throw new HandleException(409, "Bad Request", "Already exists a file with name: " + file.getOriginalFilename());
        }
        try{
            Files.copy(file.getInputStream(), storePath);
        }catch (IOException e){
            throw new HandleException(500, "FileStorageError", "Could not store file " + file.getOriginalFilename() + ". Please try again!");
        }
    }

    private Attachment createAttachmentEntity(Long expenseId, MultipartFile file, Path storePath) {
        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setStorePath(storePath.toString());
        attachment.setContentType(file.getContentType());
        attachment.setSizeBytes(file.getSize());
        attachment.setUploadDate(LocalDateTime.now());
        attachment.setExpense(expenseService.findById(expenseId));

        return attachment;
    }







}
