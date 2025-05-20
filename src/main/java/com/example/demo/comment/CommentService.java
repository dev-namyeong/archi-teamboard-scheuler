package com.example.demo.comment;

import com.example.demo.post.Post;
import com.example.demo.post.PostRepository;
import com.example.demo.user.entity.SiteUser;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.security.SiteUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 댓글 작성 (대댓글 포함)
    public void createComment(SiteUserDetails userDetails, CommentDto dto) {
        SiteUser user = userDetails.getUser();

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setUser(user);
        comment.setPost(post);

        // 부모 댓글이 있을 경우
        if (dto.getParentId() != null) {
            Comment parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다."));
            comment.setParent(parent);
        }

        commentRepository.save(comment);
    }

    //  트리 구조 댓글 반환
    public List<CommentDto> getCommentsByPostId(Long postId) {
        // 특정 게시글(postId)의 모든 댓글을 한번에 가지고 옴 (댓글, 대댓글 모두 포함)
        List<Comment> comments = commentRepository.findByPost_PostId(postId);

        List<CommentDto> dtos = comments.stream()
                .map(comment -> {
                    CommentDto dto = CommentDto.fromEntity(comment);
                    dto.setUserName(comment.getUser().getUsername());
                    return dto;
                })
                .collect(Collectors.toList());

        // 댓글 ID로 DTO 매핑
        Map<Long, CommentDto> map = new HashMap<>();
        for (CommentDto dto : dtos) {
            map.put(dto.getId(), dto);
        }

        // 부모-자식 구조 설정
        List<CommentDto> rootComments = new ArrayList<>();
        for (CommentDto dto : dtos) {
            if (dto.getParentId() != null) {
                // 대댓글이면 부모를 찾아서 children에 추가
                CommentDto parent = map.get(dto.getParentId());
                if (parent != null) {
                    parent.getChildren().add(dto);
                }
            } else {
                rootComments.add(dto);
            }
        }
        return rootComments;
    }

    // 댓글 수정
    public void updateComment(Long commentId, CommentDto dto, SiteUserDetails userDetails) {
        SiteUser currentUser = userDetails.getUser();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("댓글 작성자만 수정할 수 있습니다.");
        }

        comment.setContent(dto.getContent());
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId, SiteUserDetails userDetails) {
        SiteUser currentUser = userDetails.getUser();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("댓글 작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }
}