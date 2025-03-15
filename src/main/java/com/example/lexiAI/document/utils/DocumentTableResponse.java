package com.example.lexiAI.document.utils;


public class DocumentTableResponse {
    private int rowIndex;
    private int columnIndex;
    private String content;

    public DocumentTableResponse(int rowIndex, int columnIndex, String content) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.content = content;
    }

    public int getRowIndex() { return rowIndex; }
    public int getColumnIndex() { return columnIndex; }
    public String getContent() { return content; }
}

