package com.example.demo.submenus;

import com.example.demo.menus.MenusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class SubMenusController {

    private final SubMenusRepository subMenusRepository;
    private final MenusService menusService;

    @GetMapping("/submenu/view/{id}")
    public String viewSubMenu(@PathVariable Long id, Model model) {
        Optional<SubMenus> subMenuOpt = subMenusRepository.findById(id);

        if (subMenuOpt.isPresent()) {
            SubMenus subMenus = subMenuOpt.get();

            // 메뉴 정보도 함께 모델에 담기
            model.addAttribute("menus", menusService.findAllMenusWithSubMenus());

            // 정적 페이지가 연결된 경우
            if (subMenus.getTemplatePath() != null && !subMenus.getTemplatePath().isBlank()) {
                return subMenus.getTemplatePath();
            }

            // 게시판 연결된 경우
            if (subMenus.isBoard()) {
                model.addAttribute("subMenu", subMenus);
                return "board/list";
            }

            // 아무 것도 없으면 404
            return "error/404";
        } else {
            return "error/404";
        }
    }
}
