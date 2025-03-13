package com.example.lexiAI.document.service;


import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class DocumentStorageService {

    private final BlobContainerClient containerClient;

    public DocumentStorageService(@Value("${azure.storage.connection-string}") String connectionString,
                              @Value("${azure.storage.container-name}") String containerName) {
        this.containerClient = new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildClient();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        try (InputStream dataStream = file.getInputStream()) {
            blobClient.upload(dataStream, file.getSize(), true);
            blobClient.setHttpHeaders(new BlobHttpHeaders().setContentType(file.getContentType()));
        }


        return blobClient.getBlobUrl();
    }



    public void deleteFile(String fileName) {
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        blobClient.delete();
    }
}

