package com.example.demo.board;

import com.example.demo.submenus.SubMenus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findBySubMenu_id(Long subMenuId);
    
}