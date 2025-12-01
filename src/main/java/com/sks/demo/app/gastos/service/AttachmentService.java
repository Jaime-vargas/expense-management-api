package com.sks.demo.app.gastos.service;

import com.sks.demo.app.gastos.dto.AttachmentDTO;
import com.sks.demo.app.gastos.exeptionsHandler.HandleException;
import com.sks.demo.app.gastos.model.Attachment;

import com.sks.demo.app.gastos.repository.AttachmentRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;


@Service
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    // Root directory for file storage configured in application.properties
    @Getter
    public final Path uploadDir;

    public AttachmentService(AttachmentRepository attachmentRepository, @Value("${file.upload-dir}") String uploadDir){
        this.attachmentRepository = attachmentRepository;
        this.uploadDir = Paths.get(uploadDir);
    }

    // Methods
    public List<AttachmentDTO> getAttachmentsByExpenseId(Long expenseId) {
        List <Attachment> attachments = attachmentRepository.findByExpenseId(expenseId);
        return attachments.stream().map(this::entityToDTO).toList();
    }

    // Method to download a file
    public Resource downloadFile(Long expenseId ,Long attachmentId) {
        existsByExpenseIdAndId(expenseId, attachmentId);
        Attachment attachment = findById(attachmentId);
        Resource resource = loadFileAsResource(attachment.getStorePath(), attachment.getFileName());
        resourceIsReadable(resource);
        return resource;
    }

    // Method to add an attachment
    @Transactional
    public AttachmentDTO addAttachment(Attachment attachment) {
        attachmentRepository.save(attachment);
        return  entityToDTO(attachment);
    }

    // Method to delete an attachment
    @Transactional
    public void deleteAttachment(Long expenseID ,Long attachmentId) {
        existsByExpenseIdAndId(expenseID, attachmentId);
        Attachment attachment = findById(attachmentId);
        Path filePath = Paths.get(attachment.getStorePath());
        deleteAttachmentFile(filePath);
        deleteParentDirectoriesIfEmpty(filePath);
        attachmentRepository.deleteById(attachmentId);
    }

    // No transactional methods
    public Attachment findById(Long attachmentId) {
        return attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new HandleException(400, "Bad Request", "Attachment not found with id: " + attachmentId));
    }

    // Count attachments added to an expense
    public Long countAttachments(Long expenseId) {
        Long attachmentsAdded = attachmentRepository.countByExpenseId(expenseId);
        if (attachmentsAdded < 1) {
            return 0L;
        } else {
            return attachmentsAdded;
        }
    }

    public List<Attachment> getAttachments(Long expenseId) {
        return attachmentRepository.findByExpenseId(expenseId);
    }

    public void existsByExpenseIdAndId(Long expenseId, Long attachmentId){
        if(!attachmentRepository.existsByExpenseIdAndId(expenseId, attachmentId)){
            throw new HandleException(400, "Bad Request", "Attachment with id: " + attachmentId + " does not exist in expense with id: " + expenseId);
        }
    }

    // Generate resource from file path
    private Resource loadFileAsResource(String storePath, String fileName) {
        Path filePath = Paths.get(storePath);
        try {
            return  new UrlResource(filePath.toUri());

        }catch (MalformedURLException e) {
            throw new HandleException(409, "Conflict", "File not found: " + fileName);
        }
    }
    // Validate if resource is readable
    private void resourceIsReadable(Resource resource) {
        if (!resource.exists() || !resource.isReadable()) {
            throw new HandleException(409, "Conflict", "File not found or not readable: " + resource.getFilename());
        }
    }

    private void deleteAttachmentFile(Path filePath) {
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new HandleException(500, "FileDeletionError", "Could not delete file at path: " + filePath.toString());
        }
    }

    private void deleteParentDirectoriesIfEmpty(Path filePath) {
        Path parentDir = filePath.getParent();
        while (parentDir != null && !parentDir.equals(uploadDir)) {
            try(Stream<Path> files = Files.list(parentDir)) {
                if (Files.isDirectory(parentDir) && files.findAny().isEmpty()) {
                    Files.deleteIfExists(parentDir);
                    parentDir = parentDir.getParent();
                } else {
                    break;
                }
            } catch (IOException e) {
                throw new HandleException(500, "DirectoryDeletionError", "Could not delete empty parent directories for file at path: " + filePath.toString());
            }
        }
    }

    private AttachmentDTO entityToDTO(Attachment attachment) {
        AttachmentDTO attachmentDTO = new AttachmentDTO();
        attachmentDTO.setId(attachment.getId());
        attachmentDTO.setFileName(attachment.getFileName());
        attachmentDTO.setContentType(attachment.getContentType());
        attachmentDTO.setUploadDate(attachment.getUploadDate());
        attachmentDTO.setExpenseId(attachment.getExpense().getId());
        return attachmentDTO;
    }

}
