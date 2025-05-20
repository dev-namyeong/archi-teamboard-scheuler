package com.example.demo.file;

import com.example.demo.post.Post;
import com.example.demo.user.entity.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.file.File;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.upload.dir}")
    private String uploadDir;

    private final FileRepository fileRepository;

    public void saveFile(MultipartFile file, Post post, SiteUser user) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String newFileName = UUID.randomUUID() + "_" + originalFilename;
        Path filePath = Paths.get(uploadDir).resolve(newFileName);

        // 실제 파일 저장
        Files.createDirectories(filePath.getParent());
        file.transferTo(filePath.toFile());

        // DB 저장할 File 객체 생성
        File newFile = new File();
        newFile.setFileName(newFileName); // 서버 저장용 이름
        newFile.setOriginalFileName(originalFilename); // 원래 이름 저장
        newFile.setFilePath(filePath.toString());
        newFile.setPost(post);

        // 업로드한 사용자 설정: 여기서는 'user' 사용
        newFile.setUploadedBy(user);  // 'siteUser' 대신 'user' 사용

        newFile.setVersion("1.0");

        fileRepository.save(newFile);
    }

    public Resource loadFileAsResource(String fileName) throws MalformedURLException {
        Path filepath = Paths.get(uploadDir).resolve(fileName).normalize();
        return new UrlResource(filepath.toUri());
    }

    public void deleteFile(Long fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다."));

        try {
            Path path = Paths.get(file.getFilePath());
            Files.deleteIfExists(path);  // 파일이 없을 경우에도 예외 발생 X
        } catch (IOException e) {
            // 로그만 남기고 진행 (전체 삭제 흐름은 멈추지 않도록)
            e.printStackTrace();
        }

        fileRepository.delete(file); // DB에서 삭제
    }

}
