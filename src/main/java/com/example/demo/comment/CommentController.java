package com.example.demo.comment;

import com.example.demo.post.Post;
import com.example.demo.post.PostDto;
import com.example.demo.post.PostService;
import com.example.demo.user.security.SiteUserDetails;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    // 댓글 작성 및 대댓글 작성
    @PostMapping("/post/{postId}")
    public String createComment(@AuthenticationPrincipal SiteUserDetails userDetails,
                                @PathVariable("postId") Long postId,
                                @RequestParam("content") String content,
                                @RequestParam(value = "parentId", required = false) Long parentId) {

        CommentDto commentDto = new CommentDto();
        commentDto.setPostId(postId);
        commentDto.setContent(content);
        commentDto.setParentId(parentId); // null이면 일반 댓글, 아니면 대댓글
        commentDto.setUserId(userDetails.getUser().getUserId());
        commentDto.setUserName(userDetails.getUsername());
        commentDto.setCreatedAt(LocalDateTime.now());

        commentService.createComment(userDetails, commentDto);
        return "redirect:/posts/" + postId;
    }

    // 댓글 수정
    @PostMapping("/{commentId}/edit")
    public String updateComment(@AuthenticationPrincipal SiteUserDetails userDetails,
                                @PathVariable Long commentId,
                                @RequestParam("content") String content,
                                @RequestParam("postId") Long postId) {
        CommentDto commentDto = new CommentDto();
        commentDto.setContent(content);
        commentService.updateComment(commentId, commentDto, userDetails);
        return "redirect:/posts/" + postId;
    }

    // 댓글 삭제
    @PostMapping("/{commentId}/delete")
    public String deleteComment(@AuthenticationPrincipal SiteUserDetails userDetails,
                                @PathVariable Long commentId,
                                @RequestParam("postId") Long postId) {
        commentService.deleteComment(commentId, userDetails);
        return "redirect:/posts/" + postId;
    }
}
