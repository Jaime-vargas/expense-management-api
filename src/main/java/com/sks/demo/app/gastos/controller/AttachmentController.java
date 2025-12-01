package com.sks.demo.app.gastos.controller;

import com.sks.demo.app.gastos.dto.AttachmentDTO;
import com.sks.demo.app.gastos.model.Attachment;
import com.sks.demo.app.gastos.service.orchestrators.AttachmentOrchestrator;
import com.sks.demo.app.gastos.service.AttachmentService;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final AttachmentOrchestrator attachmentOrchestrator;

    public AttachmentController(AttachmentService attachmentService, AttachmentOrchestrator attachmentOrchestrator) {
        this.attachmentService = attachmentService;
        this.attachmentOrchestrator = attachmentOrchestrator;
    }

    // Endpoints to handle attachments for expenses
    @GetMapping("/{expenseId}/attachments")
    public ResponseEntity<List<AttachmentDTO>> getAttachmentsByExpenseId(@PathVariable Long expenseId) {
        List<AttachmentDTO> attachments = attachmentOrchestrator.getAttachmentsByExpenseId(expenseId);
        return ResponseEntity.status(HttpStatus.OK).body(attachments);
    }

    // Download attachment file
    @GetMapping("/{expenseId}/attachments/{attachmentId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long expenseId, @PathVariable Long attachmentId) {

        Resource resource = attachmentService.downloadFile(expenseId, attachmentId);
        Attachment attachment = attachmentService.findById(attachmentId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/{expenseId}/attachments")
    public ResponseEntity<AttachmentDTO> addAttachment(@PathVariable Long expenseId, @RequestPart("file") MultipartFile file) {
        AttachmentDTO savedAttachment = attachmentOrchestrator.addAttachment(expenseId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAttachment);
    }

    @DeleteMapping("/{expenseId}/attachments/{attachmentId}")
    public ResponseEntity<?> deleteAttachment(@PathVariable Long expenseId, @PathVariable Long attachmentId) {
        attachmentService.deleteAttachment(expenseId, attachmentId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Attachment with id " + attachmentId + " has been deleted"));
    }

}

