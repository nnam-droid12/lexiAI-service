package com.example.lexiAI.document.service;

import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClient;
import com.azure.ai.formrecognizer.documentanalysis.DocumentAnalysisClientBuilder;
import com.azure.ai.formrecognizer.documentanalysis.models.AnalyzeResult;
import com.azure.ai.formrecognizer.documentanalysis.models.DocumentTable;
import com.azure.ai.formrecognizer.documentanalysis.models.OperationResult;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.polling.SyncPoller;
import com.example.lexiAI.document.utils.DocumentAnalysisResponse;
import com.example.lexiAI.document.utils.DocumentTableResponse;
import com.example.lexiAI.speech.service.TextToSpeechService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DocumentAnalysisService {

    private final DocumentAnalysisClient documentAnalysisClient;
    private final TextToSpeechService textToSpeechService;

    public DocumentAnalysisService(@Value("${azure.document-intelligence.endpoint}") String endpoint,
                                   @Value("${azure.document-intelligence.api-key}") String apiKey, TextToSpeechService textToSpeechService) {
        this.textToSpeechService = textToSpeechService;
        this.documentAnalysisClient = new DocumentAnalysisClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(apiKey))
                .buildClient();
    }

    public DocumentAnalysisResponse analyzeDocumentFromUrl(String fileUrl) {
        SyncPoller<OperationResult, AnalyzeResult> poller =
                documentAnalysisClient.beginAnalyzeDocumentFromUrl("prebuilt-document", fileUrl);

        AnalyzeResult result = poller.getFinalResult();

        // Extract Key-Value Pairs
        Map<String, String> keyValuePairs = Optional.ofNullable(result.getKeyValuePairs())
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.toMap(
                        pair -> pair.getKey() != null ? pair.getKey().getContent() : "Unknown Key",
                        pair -> pair.getValue() != null ? pair.getValue().getContent() : "Unknown Value"
                ));

        // Extract Paragraphs
        List<String> paragraphs = Optional.ofNullable(result.getParagraphs())
                .orElse(Collections.emptyList())
                .stream()
                .map(paragraph -> paragraph.getContent())
                .collect(Collectors.toList());

        // Extract Tables
        List<DocumentTableResponse> tables = new ArrayList<>();
        if (result.getTables() != null) {
            for (DocumentTable table : result.getTables()) {
                table.getCells().forEach(cell ->
                        tables.add(new DocumentTableResponse(
                                cell.getRowIndex(), cell.getColumnIndex(), cell.getContent()
                        ))
                );
            }
        }

        return new DocumentAnalysisResponse(keyValuePairs, paragraphs, tables);
    }



    public ByteArrayResource synthesizeExtractedText(DocumentAnalysisResponse analysisResponse) throws Exception {
        StringBuilder textToRead = new StringBuilder();

        textToRead.append("Extracted Key Clauses:\n");
        analysisResponse.getKeyValuePairs().forEach((key, value) ->
                textToRead.append(key).append(": ").append(value).append("\n")
        );

        textToRead.append("\nExtracted Paragraphs:\n");
        analysisResponse.getParagraphs().forEach(paragraph ->
                textToRead.append(paragraph).append("\n")
        );


        return textToSpeechService.synthesizeSpeech(textToRead.toString());
    }

}