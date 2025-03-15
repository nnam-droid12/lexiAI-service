package com.example.lexiAI.documentTranslation.controller;


import com.example.lexiAI.documentTranslation.service.TranslationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/document")
public class TranslationController {

    private final TranslationService translationService;

    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping("/translate-document")
    public ResponseEntity<?> translateDocument(@RequestBody Map<String, String> request) {
        try {
            String fileUrl = request.get("fileUrl");
            String targetLanguage = request.get("targetLanguage");

            if (fileUrl == null || fileUrl.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "fileUrl is required");
                return ResponseEntity.badRequest().body(error);
            }

            if (targetLanguage == null || targetLanguage.isEmpty()) {
                // Default to French if not specified
                targetLanguage = "fr";
            }

            System.out.println("Processing translation request for: " + fileUrl + " to " + targetLanguage);

            String translatedText = translationService.translateDocumentText(fileUrl, targetLanguage);

            Map<String, String> response = new HashMap<>();
            response.put("translatedText", translatedText);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Translation failed: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }


}
