package com.example.lexiAI.document.service;

import com.example.lexiAI.document.entity.Document;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentSearchService {

    private final EntityManager entityManager;

    public DocumentSearchService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Document> searchByFileName(String searchTerm) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Document> criteriaQuery = criteriaBuilder.createQuery(Document.class);
        Root<Document> root = criteriaQuery.from(Document.class);

        Predicate fileNamePredicate = criteriaBuilder.like(
                criteriaBuilder.lower(root.get("fileName")),
                "%" + searchTerm.toLowerCase() + "%"
        );

        criteriaQuery.where(fileNamePredicate);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

}