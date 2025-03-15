package com.example.lexiAI.document.controller;


import com.example.lexiAI.document.service.DocumentAnalysisService;
import com.example.lexiAI.document.utils.DocumentAnalysisResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    public ResponseEntity<DocumentAnalysisResponse> analyzeDocument(@RequestParam("fileUrl") String fileUrl) {
        try {
            return ResponseEntity.ok(documentAnalysisService.analyzeDocumentFromUrl(fileUrl));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/read")
    public ResponseEntity<ByteArrayResource> readExtractedText(@RequestBody DocumentAnalysisResponse analysisResponse) {
        try {
            ByteArrayResource audioResource = documentAnalysisService.synthesizeExtractedText(analysisResponse);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=speech.wav")
                    .contentType(MediaType.parseMediaType("audio/wav"))
                    .body(audioResource);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}

