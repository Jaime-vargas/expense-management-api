package com.sks.demo.app.gastos.controller;

import com.sks.demo.app.gastos.exeptionsHandler.HandleException;
import com.sks.demo.app.gastos.service.DocumentService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@RestController
@RequestMapping("/api")
public class DocumentResourceController {

    DocumentService documentService;

    public DocumentResourceController (DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/getreport/{idProject}")
    public ResponseEntity<byte[]> getReport(@PathVariable Long idProject) {

        byte[] pdf = documentService.generatePdf(idProject);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"report.pdf\"");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }


    @GetMapping("/exportreport/{idProject}")
    public ResponseEntity<byte[]> exportReport(@PathVariable Long idProject) {

        byte[] pdf = documentService.generatePdf(idProject);
        List<File> attachments = documentService.getAttachmentsAsFiles(idProject);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zipOut = new ZipOutputStream(baos)) {

            // Agregar el PDF como primer archivo dentro del ZIP
            ZipEntry pdfEntry = new ZipEntry("report.pdf");
            zipOut.putNextEntry(pdfEntry);
            zipOut.write(pdf);
            zipOut.closeEntry();

            // Agregar attachments
            for (File attachment : attachments) {
                try (FileInputStream fis = new FileInputStream(attachment)) {
                    ZipEntry zipEntry = new ZipEntry(attachment.getName());
                    zipOut.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) >= 0) {
                        zipOut.write(buffer, 0, length);
                    }

                    zipOut.closeEntry();
                } catch (IOException e) {
                    throw new HandleException(
                            500,
                            "Error creating zip",
                            "Error reading attachment file: " + attachment.getName()
                    );
                }
            }

            zipOut.finish();

            byte[] zipBytes = baos.toByteArray();
            // 1. Crear Content-Disposition de forma robusta
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename("reporte_completo.zip", StandardCharsets.UTF_8)
                    .build();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(contentDisposition);
            headers.setContentLength(zipBytes.length);
            headers.set("Access-Control-Expose-Headers",
                    "X-Total-Count, Authorization");


            return ResponseEntity.ok()
                    .headers(headers)
                    .body(zipBytes);

        } catch (IOException e) {
            throw new HandleException(500, "Error creating zip", "Cannot generate ZIP");
        }
    }
}
