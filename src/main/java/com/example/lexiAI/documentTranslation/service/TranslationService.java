package com.example.lexiAI.documentTranslation.service;


import com.example.lexiAI.document.service.DocumentAnalysisService;
import com.example.lexiAI.document.utils.DocumentAnalysisResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class TranslationService {

    @Value("${azure.translation.api-key}")
    private String apiKey;

    @Value("${azure.translation.endpoint}")
    private String endpoint;

    @Value("${azure.translation.region}")
    private String region;

    private final RestTemplate restTemplate = new RestTemplate();
    private final DocumentAnalysisService documentAnalysisService;

    public TranslationService(DocumentAnalysisService documentAnalysisService) {
        this.documentAnalysisService = documentAnalysisService;
    }

    public String translateDocumentText(String fileUrl, String targetLanguage) {
        try {
            // Step 1: Extract text from the document
            DocumentAnalysisResponse analysisResponse = documentAnalysisService.analyzeDocumentFromUrl(fileUrl);

            // Step 2: Prepare text for translation (combining paragraphs, which contain the main content)
            StringBuilder textToTranslate = new StringBuilder();

            // Add paragraphs
            for (String paragraph : analysisResponse.getParagraphs()) {
                textToTranslate.append(paragraph).append("\n\n");
            }

            String extractedText = textToTranslate.toString().trim();

            if (extractedText.isEmpty()) {
                return "No text found in the document.";
            }

            // Step 3: Translate the extracted text
            return translateText(extractedText, targetLanguage);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing document: " + e.getMessage();
        }
    }

    private String translateText(String text, String targetLanguage) {
        try {
            String url = endpoint + "/translate?api-version=3.0&to=" + targetLanguage;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Ocp-Apim-Subscription-Key", apiKey);
            headers.set("Ocp-Apim-Subscription-Region", region);

            // Azure Translator expects an array of objects with a "Text" property
            List<Map<String, String>> requestBody = Collections.singletonList(
                    Collections.singletonMap("Text", text)
            );

            HttpEntity<List<Map<String, String>>> request = new HttpEntity<>(requestBody, headers);

            System.out.println("Sending translation request to: " + url);
            System.out.println("Text length: " + text.length() + " characters");

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return extractTranslatedText(response.getBody());
            } else {
                return "Translation API error: " + response.getStatusCode() + " - " + response.getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Translation failed: " + e.getMessage();
        }
    }



    private String extractTranslatedText(String response) {
        try {
            // Using a simple JSON processing approach
            // This is a basic implementation - consider using a proper JSON library like Jackson
            if (response == null || response.isEmpty()) {
                return "No translation returned";
            }

            // Extract the translated text from the JSON response
            // Format is typically: [{"translations":[{"text":"translated text","to":"language code"}]}]
            if (response.contains("\"text\":\"")) {
                String[] parts = response.split("\"text\":\"");
                if (parts.length > 1) {
                    return parts[1].split("\",\"")[0]
                            .replace("\\\"", "\"")  // Handle escaped quotes
                            .replace("\\\\", "\\"); // Handle escaped backslashes
                }
            }
            return "Could not parse translation response: " + response;
        } catch (Exception e) {
            return "Error extracting translated text: " + e.getMessage();
        }
    }



    // For large documents, you may need to split the text
    private String translateLargeText(String text, String targetLanguage) {
        // Azure Translator has a limit on request size (typically around 5000 chars)
        final int MAX_CHUNK_SIZE = 4500;

        if (text.length() <= MAX_CHUNK_SIZE) {
            return translateText(text, targetLanguage);
        }

        // Split text into manageable chunks at paragraph boundaries
        List<String> chunks = new ArrayList<>();
        StringBuilder currentChunk = new StringBuilder();

        String[] paragraphs = text.split("\n\n");
        for (String paragraph : paragraphs) {
            if (currentChunk.length() + paragraph.length() > MAX_CHUNK_SIZE) {
                // Current chunk would exceed limit, save it and start a new one
                chunks.add(currentChunk.toString());
                currentChunk = new StringBuilder();
            }
            currentChunk.append(paragraph).append("\n\n");
        }

        // Add the last chunk if not empty
        if (currentChunk.length() > 0) {
            chunks.add(currentChunk.toString());
        }

        // Translate each chunk and combine
        StringBuilder result = new StringBuilder();
        for (String chunk : chunks) {
            result.append(translateText(chunk, targetLanguage));
        }

        return result.toString();
    }




}
