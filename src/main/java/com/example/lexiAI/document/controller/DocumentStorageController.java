package com.example.lexiAI.document.controller;



import com.example.lexiAI.document.service.DocumentStorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/document")
public class DocumentStorageController {

    private final DocumentStorageService documentStorageService;

    public DocumentStorageController(DocumentStorageService documentStorageService) {
        this.documentStorageService = documentStorageService;
    }


    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(documentStorageService.uploadFile(file));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        documentStorageService.deleteFile(fileName);
        return ResponseEntity.ok("File deleted successfully.");
    }
}

