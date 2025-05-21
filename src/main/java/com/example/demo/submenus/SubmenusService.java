package com.example.demo.submenus;

import com.example.demo.board.Board;
import com.example.demo.board.BoardRepository;
import com.example.demo.department.Department;
import com.example.demo.menus.Menus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubmenusService {

    private final SubMenusRepository subMenusRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public SubMenus createSubMenuWithBoard(Menus menu, String name, boolean isBoard, List<Department> uploadDepartments, String templatePath) {
        SubMenus subMenus = new SubMenus(menu, name, isBoard, uploadDepartments, templatePath);

        // 서브메뉴 먼저 저장 (ID 필요)
        subMenus = subMenusRepository.save(subMenus);

        if (isBoard) {
            Board board = new Board();
            board.setName(name + " 게시판");
            board.setSubMenu(subMenus);

            boardRepository.save(board);
        }

        return subMenus;
    }

    @Transactional
    public void deactivateSubMenu(Long subMenuId) {
        SubMenus subMenus = subMenusRepository.findById(subMenuId)
                .orElseThrow(() -> new IllegalArgumentException("서브메뉴를 찾을 수 없습니다."));
        subMenus.setActive(false);
        subMenusRepository.save(subMenus);
    }

    @Transactional
    public void reactivateSubMenu(Long subMenuId) {
        SubMenus subMenus = subMenusRepository.findById(subMenuId)
                .orElseThrow(() -> new IllegalArgumentException("서브메뉴를 찾을 수 없습니다."));
        subMenus.setActive(true);
        subMenusRepository.save(subMenus);
    }

    @Transactional
    public void deleteSubMenu(Long subMenuId) {
        SubMenus subMenus = subMenusRepository.findById(subMenuId)
                .orElseThrow(() -> new IllegalArgumentException("서브메뉴를 찾을 수 없습니다."));

        subMenusRepository.delete(subMenus); // 물리 삭제
    }
}
