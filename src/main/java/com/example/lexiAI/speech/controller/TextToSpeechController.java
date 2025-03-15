package com.example.lexiAI.speech.controller;



import com.example.lexiAI.speech.service.TextToSpeechService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/speech")
public class TextToSpeechController {

    private final TextToSpeechService textToSpeechService;

    public TextToSpeechController(TextToSpeechService textToSpeechService) {
        this.textToSpeechService = textToSpeechService;
    }

    @PostMapping("/synthesize")
    public ResponseEntity<ByteArrayResource> synthesizeSpeech(@RequestParam String text) {
        try {
            ByteArrayResource audioResource = textToSpeechService.synthesizeSpeech(text);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"speech.wav\"")
                    .contentType(MediaType.parseMediaType("audio/wav"))
                    .contentLength(audioResource.contentLength())
                    .body(audioResource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
