package com.example.lexiAI.document.repository;

import com.example.lexiAI.document.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Optional<Document> findByFileName(String fileName);

    List<Document> findByFileNameContainingIgnoreCase(String fileName);

    void deleteByFileName(String fileName);
}
