package com.sks.demo.app.gastos.repository;

import com.sks.demo.app.gastos.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository <Attachment, Long> {
    List<Attachment> findByExpenseId(Long expenseId);
    Long countByExpenseId(Long expenseId);
    void deleteByExpenseId(Long expenseId);
    boolean existsByExpenseIdAndId(Long expenseId, Long id);
}
