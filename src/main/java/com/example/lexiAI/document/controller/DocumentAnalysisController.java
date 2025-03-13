package com.example.lexiAI.document.controller;


import com.example.lexiAI.document.service.DocumentAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/document")
public class DocumentAnalysisController {

    private final DocumentAnalysisService documentAnalysisService;

    public DocumentAnalysisController(DocumentAnalysisService documentAnalysisService) {
        this.documentAnalysisService = documentAnalysisService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeDocument(@RequestParam("fileUrl") String fileUrl) {
        try {
            String analysisResult = documentAnalysisService.analyzeDocumentFromUrl(fileUrl);
            return ResponseEntity.ok(analysisResult);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error analyzing document: " + e.getMessage());
        }
    }
}

