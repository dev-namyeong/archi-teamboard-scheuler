package com.example.demo.file;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    // 파일 다운로드
    @GetMapping("/files/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws MalformedURLException {
        Resource resource = fileService.loadFileAsResource(fileName);

        // 파일 존재 여부 확인
        if (!resource.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다.");
        }

        // 파일명 한글 깨짐 방지
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .body(resource);
    }

    // 이미지 미리보기
    @GetMapping("/files/view/{fileName}")
    public ResponseEntity<Resource> viewFile(@PathVariable String fileName) throws MalformedURLException {
        Resource resource = fileService.loadFileAsResource(fileName);

        if (!resource.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다.");
        }

        // 확장자에 따라 Content-Type 설정
        String contentType;
        String lowerFileName = fileName.toLowerCase();

        if (lowerFileName.endsWith(".jpg") || lowerFileName.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (lowerFileName.endsWith(".png")) {
            contentType = "image/png";
        } else if (lowerFileName.endsWith(".gif")) {
            contentType = "image/gif";
        } else if (lowerFileName.endsWith(".pdf")) {
            contentType = "application/pdf";
        } else if (lowerFileName.endsWith(".txt") || lowerFileName.endsWith(".csv") || lowerFileName.endsWith(".log")) {
            contentType = "text/plain";
        } else {
            contentType = "application/octet-stream"; // 그 외 확장자는 다운로드
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }

}