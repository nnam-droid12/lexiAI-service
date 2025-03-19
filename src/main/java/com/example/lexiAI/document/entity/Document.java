package com.example.lexiAI.document.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileType;

    private String fileUrl;

    private LocalDateTime uploadDate;

    public Document() {
    }

    public Long getId() {
        return this.id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getFileType() {
        return this.fileType;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public LocalDateTime getUploadDate() {
        return this.uploadDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Document)) return false;
        final Document other = (Document) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$fileName = this.getFileName();
        final Object other$fileName = other.getFileName();
        if (this$fileName == null ? other$fileName != null : !this$fileName.equals(other$fileName)) return false;
        final Object this$fileType = this.getFileType();
        final Object other$fileType = other.getFileType();
        if (this$fileType == null ? other$fileType != null : !this$fileType.equals(other$fileType)) return false;
        final Object this$fileUrl = this.getFileUrl();
        final Object other$fileUrl = other.getFileUrl();
        if (this$fileUrl == null ? other$fileUrl != null : !this$fileUrl.equals(other$fileUrl)) return false;
        final Object this$uploadDate = this.getUploadDate();
        final Object other$uploadDate = other.getUploadDate();
        if (this$uploadDate == null ? other$uploadDate != null : !this$uploadDate.equals(other$uploadDate))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Document;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $fileName = this.getFileName();
        result = result * PRIME + ($fileName == null ? 43 : $fileName.hashCode());
        final Object $fileType = this.getFileType();
        result = result * PRIME + ($fileType == null ? 43 : $fileType.hashCode());
        final Object $fileUrl = this.getFileUrl();
        result = result * PRIME + ($fileUrl == null ? 43 : $fileUrl.hashCode());
        final Object $uploadDate = this.getUploadDate();
        result = result * PRIME + ($uploadDate == null ? 43 : $uploadDate.hashCode());
        return result;
    }

    public String toString() {
        return "Document(id=" + this.getId() + ", fileName=" + this.getFileName() + ", fileType=" + this.getFileType() + ", fileUrl=" + this.getFileUrl() + ", uploadDate=" + this.getUploadDate() + ")";
    }
}