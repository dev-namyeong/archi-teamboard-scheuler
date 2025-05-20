package com.example.demo.file;

import lombok.Data;
import com.example.demo.file.File;

import java.time.LocalDateTime;

@Data
public class FileDto {
    private Long fileId;
    private String fileName;
    private String originalFileName;
    private String filePath;
    private String version;
    private Long uploadedByUserId;
    private String uploadedByUserName;
    private LocalDateTime uploadedAt;
    private String ext;

    public FileDto(String fileName, String originalFileName) {
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        if (fileName != null && fileName.contains(".")) {
            this.ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        }
    }

    public static FileDto fromEntity(File file) {
        FileDto dto = new FileDto(file.getFileName(), file.getOriginalFileName());
        dto.setFileId(file.getFileId());
        dto.setFilePath(file.getFilePath());
        dto.setVersion(file.getVersion());
        dto.setUploadedAt(file.getUploadedAt());

        if (file.getUploadedBy() != null) {
            dto.setUploadedByUserId(file.getUploadedBy().getUserId());
            dto.setUploadedByUserName(file.getUploadedBy().getName());
        }

        return dto;
    }
}
