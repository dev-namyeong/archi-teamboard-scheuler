package com.example.demo.comment;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private Long postId;
    private Long userId;
    private String userName;
    private Long parentId; // 대댓글이면 부모 댓글 ID
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<CommentDto> children = new ArrayList<>(); // 자식 댓글 목록

    public static CommentDto fromEntity(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getCommentId());
        dto.setContent(comment.getContent());
        dto.setPostId(comment.getPost().getPostId());
        dto.setUserId(comment.getUser().getUserId());
        dto.setUserName(comment.getUser().getUsername());
        dto.setParentId(comment.getParent() != null ? comment.getParent().getCommentId() : null);
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        return dto;
    }
}
