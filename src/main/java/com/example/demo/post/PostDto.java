package com.example.demo.post;

import com.example.demo.comment.CommentDto;
import com.example.demo.file.FileDto;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.file.File;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private Long boardId;
    private String userName;
    private LocalDateTime createdAt;
    private List<FileDto> files; // DB에 저장된 파일 정보
    private List<CommentDto> comments; // 댓글들
    private Long subMenuId; // 게시글 작성 후 리다이렉트를 위한 서브메뉴 ID
    private LocalDateTime updatedAt;

    // 작성 시 파일 업로드용
    private List<MultipartFile> multipartFiles;

    public static PostDto fromEntity(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getPostId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setUserId(post.getUser().getUserId());
        dto.setBoardId(post.getBoard().getBoardId());
        dto.setUserName(post.getUser().getName());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setSubMenuId(post.getBoard().getSubMenu().getId());
        dto.setUpdatedAt(post.getUpdatedAt());

        // FileDto 매핑
        if (post.getFiles() != null) {
            dto.setFiles(post.getFiles().stream()
                    .map(FileDto::fromEntity)
                    .collect(Collectors.toList()));
        }
        // CommentDto 매핑
        if (post.getComments() != null) {
            dto.setComments(post.getComments().stream()
                    .map(CommentDto::fromEntity)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
