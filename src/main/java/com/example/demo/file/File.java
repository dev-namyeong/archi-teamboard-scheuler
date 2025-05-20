package com.example.demo.file;

import com.example.demo.post.Post;
import com.example.demo.user.entity.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    private String fileName; // 서버에 저장된 이름 (UUID 포함)
    private String originalFileName; // 사용자가 업로드한 원래 이름

    private String filePath;

    private String version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private SiteUser uploadedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(updatable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();
}
