package com.example.demo.board;

import com.example.demo.post.Post;
import com.example.demo.post.PostDto;
import com.example.demo.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final PostRepository postRepository;

    public Board getBoardBySubMenuId(Long subMenuId) {
        return boardRepository.findBySubMenu_id(subMenuId)
                .orElseThrow(() -> new RuntimeException("해당 하위 메뉴에 대한 게시판이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public List<PostDto> getPostsBySubMenu(Long subMenuId) {
        Board board = getBoardBySubMenuId(subMenuId);
        List<Post> posts = postRepository.findByBoardOrderByCreatedAtDesc(board);

        return posts.stream().map(post -> {
            PostDto dto = PostDto.fromEntity(post);

            // Lazy 로딩 방지용: 부서 접근 → 미리 초기화
            if (post.getUser() != null && post.getUser().getDepartment() != null) {
                post.getUser().getDepartment().getName();
            }

            return dto;
        }).collect(Collectors.toList());
    }

}
