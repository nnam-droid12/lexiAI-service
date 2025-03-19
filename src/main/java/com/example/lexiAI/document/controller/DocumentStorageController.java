package com.example.lexiAI.document.controller;



import com.example.lexiAI.document.entity.Document;
import com.example.lexiAI.document.service.DocumentSearchService;
import com.example.lexiAI.document.service.DocumentStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/document")
public class DocumentStorageController {

    private final DocumentStorageService documentStorageService;
    private final DocumentSearchService documentSearchService;

    public DocumentStorageController(DocumentStorageService documentStorageService, DocumentSearchService documentSearchService) {
        this.documentStorageService = documentStorageService;
        this.documentSearchService = documentSearchService;
    }


    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(documentStorageService.uploadFile(file));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Map<String, String>>> searchDocuments(@RequestParam("fileName") String fileName) {
        List<Map<String, String>> results = documentStorageService.searchDocumentsByFileName(fileName);
        return ResponseEntity.ok(results);
    }


    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        documentStorageService.deleteFile(fileName);
        return ResponseEntity.ok("File deleted successfully.");
    }


    @GetMapping("/get-all-document")
    public ResponseEntity<List<Map<String, String>>> getAllDocuments() {
        return ResponseEntity.ok(documentStorageService.getAllDocuments());
    }


    @GetMapping("/get-document-byName/{fileName}")
    public ResponseEntity<Map<String, String>> getDocumentById(@PathVariable String fileName) {
        try {
            return ResponseEntity.ok(documentStorageService.getDocumentById(fileName));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}

