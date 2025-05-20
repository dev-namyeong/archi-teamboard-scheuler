package com.example.demo.site;

import com.example.demo.menus.MenusService;
import com.example.demo.submenus.SubMenus;
import com.example.demo.submenus.SubMenusRepository;
import com.example.demo.user.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class SubMenuPageController extends BaseController {

    private final SubMenusRepository subMenusRepository;

    // 생성자
    public SubMenuPageController(MenusService menusService, SubMenusRepository subMenusRepository) {
        super(menusService, subMenusRepository);
        this.subMenusRepository = subMenusRepository;
    }

    // 공통 서브메뉴 페이지 처리
    @GetMapping("/site/{subMenuId}")
    public String sitePage(@PathVariable Long subMenuId, Model model) {
        addMenusToModel(model);

        Optional<SubMenus> optionalSubMenus = subMenusRepository.findById(subMenuId);
        if (optionalSubMenus.isEmpty()) {
            return "error/404";
        }

        model.addAttribute("subMenu", optionalSubMenus.get());

        return "site/" + optionalSubMenus.get().getTemplatePath();
    }
}
