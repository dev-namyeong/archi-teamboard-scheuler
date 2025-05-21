package com.example.demo.admin;

import com.example.demo.department.Department;
import com.example.demo.department.DepartmentInitializer;
import com.example.demo.department.DepartmentRepository;
import com.example.demo.menus.Menus;
import com.example.demo.menus.MenusRepository;
import com.example.demo.submenus.SubMenus;
import com.example.demo.submenus.SubMenusRepository;
import com.example.demo.submenus.SubmenusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/submenus")
@RequiredArgsConstructor
public class AdminSubMenuController {

    private final MenusRepository menusRepository;
    private final SubMenusRepository subMenusRepository;
    private final SubmenusService submenusService;
    private final DepartmentRepository departmentRepository;

    // 하위 메뉴 생성 폼
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("menus", menusRepository.findAll());
        model.addAttribute("departments", departmentRepository.findAll());
        return "admin/create-submenu"; // 폼 템플릿
    }

    // 하위 메뉴 생성 처리
    @PostMapping("/create")
    public String createSubMenu(
            @RequestParam Long menuId,
            @RequestParam String name,
            @RequestParam(required = false) boolean isBoard,
            @RequestParam(name = "uploadScope") List<String> uploadScopeIds,
            @RequestParam(required = false) String templatePath) {

        Menus menu = menusRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상위 메뉴가 존재하지 않습니다."));

        List<Department> uploadDepartments;

        if (uploadScopeIds.contains("ALL")) {
            uploadDepartments = departmentRepository.findAll(); // ✅ 전체 부서로 처리
        } else {
            uploadDepartments = uploadScopeIds.stream()
                    .map(Long::parseLong)
                    .map(id -> departmentRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("부서를 찾을 수 없습니다: " + id)))
                    .toList();
        }

        submenusService.createSubMenuWithBoard(menu, name, isBoard, uploadDepartments, templatePath);

        return "redirect:/home";
    }

    @GetMapping("/list")
    public String listSubMenus(Model model) {
        List<SubMenus> all = subMenusRepository.findAll();
        model.addAttribute("submenus", all);
        return "admin/list-submenus";
    }

    // 비활성화 처리
    @PostMapping("/deactivate/{id}")
    public String deactivateSubMenu(@PathVariable Long id) {
        submenusService.deactivateSubMenu(id);
        return "redirect:/admin/submenus/list";
    }

    // 재활성화 처리
    @PostMapping("/reactivate/{id}")
    public String reactivateSubMenu(@PathVariable Long id) {
        submenusService.reactivateSubMenu(id);
        return "redirect:/admin/submenus/list";
    }

    // 물리 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteSubMenu(@PathVariable Long id) {
        submenusService.deleteSubMenu(id);
        return "redirect:/admin/submenus/list";
    }
}

