package com.example.lexiAI.document.utils;


import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentAnalysisResponse {
    private Map<String, String> keyValuePairs;
    private List<String> paragraphs;
    private List<DocumentTableResponse> tables;

    public DocumentAnalysisResponse(Map<String, String> keyValuePairs, List<String> paragraphs, List<DocumentTableResponse> tables) {
        this.keyValuePairs = keyValuePairs;
        this.paragraphs = paragraphs;
        this.tables = tables;
    }

    public Map<String, String> getKeyValuePairs() { return keyValuePairs; }
    public List<String> getParagraphs() { return paragraphs; }
    public List<DocumentTableResponse> getTables() { return tables; }
}

