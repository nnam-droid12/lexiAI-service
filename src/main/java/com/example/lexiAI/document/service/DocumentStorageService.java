package com.example.lexiAI.document.service;


import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobItem;
import com.example.lexiAI.document.entity.Document;
import com.example.lexiAI.document.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocumentStorageService {

    private final BlobContainerClient containerClient;
    private final DocumentRepository documentRepository;

    public DocumentStorageService(@Value("${azure.storage.connection-string}") String connectionString,
                                  @Value("${azure.storage.container-name}") String containerName,
                                  DocumentRepository documentRepository) {
        this.containerClient = new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildClient();
        this.documentRepository = documentRepository;
    }

    public Map<String, String> uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        try (InputStream dataStream = file.getInputStream()) {
            blobClient.upload(dataStream, file.getSize(), true);
            blobClient.setHttpHeaders(new BlobHttpHeaders().setContentType(file.getContentType()));
        }


        Document document = new Document();
        document.setFileName(fileName);
        document.setFileType(file.getContentType());
        document.setFileUrl(blobClient.getBlobUrl());
        document.setUploadDate(LocalDateTime.now());
        documentRepository.save(document);

        Map<String, String> response = new HashMap<>();
        response.put("fileName", fileName);
        response.put("fileUrl", blobClient.getBlobUrl());

        return response;
    }


    public List<Map<String, String>> searchDocumentsByFileName(String searchTerm) {
        List<Map<String, String>> results = new ArrayList<>();

        String lowerSearchTerm = searchTerm.toLowerCase();


        for (BlobItem blobItem : containerClient.listBlobs()) {
            String blobName = blobItem.getName();


            if (blobName.toLowerCase().contains(lowerSearchTerm)) {
                BlobClient blobClient = containerClient.getBlobClient(blobName);
                Map<String, String> fileData = new HashMap<>();
                fileData.put("fileName", blobName);
                fileData.put("fileUrl", blobClient.getBlobUrl());
                results.add(fileData);
            }
        }

        return results;
    }


    public void deleteFile(String fileName) {
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        blobClient.delete();
        documentRepository.deleteByFileName(fileName);
    }


    public List<Map<String, String>> getAllDocuments() {
        List<Map<String, String>> fileList = new ArrayList<>();

        for (BlobItem blobItem : containerClient.listBlobs()) {
            BlobClient blobClient = containerClient.getBlobClient(blobItem.getName());
            Map<String, String> fileData = new HashMap<>();
            fileData.put("fileName", blobItem.getName());
            fileData.put("fileUrl", blobClient.getBlobUrl());
            fileList.add(fileData);
        }

        return fileList;
    }


    public Map<String, String> getDocumentById(String fileName) {
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        if (!blobClient.exists()) {
            throw new RuntimeException("Document not found: " + fileName);
        }

        Map<String, String> fileData = new HashMap<>();
        fileData.put("fileName", fileName);
        fileData.put("fileUrl", blobClient.getBlobUrl());

        return fileData;
    }
}

