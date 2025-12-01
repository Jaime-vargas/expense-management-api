package com.sks.demo.app.gastos.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class AttachmentDTO {
    Long id;
    String fileName;
    String contentType;
    LocalDateTime uploadDate;
    Long expenseId;
}
