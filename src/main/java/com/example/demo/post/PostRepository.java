package com.example.demo.post;

import com.example.demo.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findTop5ByBoard_SubMenu_NameOrderByCreatedAtDesc(String subMenuName);

    List<Post> findByBoardOrderByCreatedAtDesc(Board board);

    List<Post> findByBoardBoardId(Long boardId);
}
