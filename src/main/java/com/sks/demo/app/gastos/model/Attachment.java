package com.sks.demo.app.gastos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor

@Entity
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String storePath;
    private String contentType;
    private Long sizeBytes;
    private LocalDateTime uploadDate;

    @ManyToOne
    @JoinColumn(name = "expense_id")
    private Expense expense;
}
