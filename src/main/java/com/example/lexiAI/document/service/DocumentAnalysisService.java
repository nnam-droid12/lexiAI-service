package com.example.lexiAI.document.service;


import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder;
import com.azure.ai.formrecognizer.documentanalysis.models.AnalyzeResult;
import com.azure.ai.formrecognizer.documentanalysis.models.DocumentTable;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentAnalysisService {

    private final DocumentAnalysisClient documentAnalysisClient;

    public DocumentAnalysisService(@Value("${azure.document-intelligence.endpoint}") String endpoint,
                                   @Value("${azure.document-intelligence.api-key}") String apiKey) {
        this.documentAnalysisClient = new DocumentAnalysisClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(apiKey))
                .buildClient();
    }

    public String analyzeDocumentFromUrl(String fileUrl) {
        // Analyze using the prebuilt 'document' model for extracting key clauses
        AnalyzeResult result = documentAnalysisClient.beginAnalyzeDocumentFromUrl("prebuilt-document", fileUrl)
                .getFinalResult();

        StringBuilder extractedInfo = new StringBuilder();

        // Extract key clauses
        extractedInfo.append("📌 **Extracted Key Clauses:**\n");
        result.getKeyValuePairs().forEach(pair -> {
            String key = (pair.getKey() != null) ? pair.getKey().getContent() : "Unknown Key";
            String value = (pair.getValue() != null) ? pair.getValue().getContent() : "Unknown Value";
            extractedInfo.append("🔹 ").append(key).append(": ").append(value).append("\n");
        });

        // Extract paragraphs
        extractedInfo.append("\n📌 **Extracted Paragraphs:**\n");
        result.getParagraphs().forEach(paragraph ->
                extractedInfo.append("🔹 ").append(paragraph.getContent()).append("\n")
        );

        // Extract tables
        extractedInfo.append("\n📌 **Extracted Tables:**\n");
        for (DocumentTable table : result.getTables()) {
            extractedInfo.append("\n🔹 **Table:**\n");
            table.getCells().forEach(cell ->
                    extractedInfo.append("Cell [").append(cell.getRowIndex()).append(", ")
                            .append(cell.getColumnIndex()).append("]: ").append(cell.getContent()).append("\n")
            );
        }

        return extractedInfo.toString();
    }
}

