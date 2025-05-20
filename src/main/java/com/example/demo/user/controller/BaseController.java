package com.example.demo.user.controller;

import com.example.demo.menus.Menus;
import com.example.demo.menus.MenusService;
import com.example.demo.submenus.SubMenusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;

import java.util.List;

@RequiredArgsConstructor
public class BaseController {

    private final MenusService menusService;
    private final SubMenusRepository subMenusRepository;


    // 공통으로 메뉴 추가
    protected void addMenusToModel(Model model) {
        // 메뉴 정보 가져오기
        List<Menus> menus = menusService.getAllMenus();
        model.addAttribute("menus", menus);

        // 서브 메뉴 정보 가져오기
        model.addAttribute("subMenus", subMenusRepository.findAll());

    }
}
