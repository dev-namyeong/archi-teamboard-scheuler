package com.example.demo.user.controller;

import com.example.demo.department.DepartmentRepository;
import com.example.demo.user.dto.UserDto;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final DepartmentRepository departmentRepository;
    private final UserService userService;

    @GetMapping("/signup")
    public String showSignupForm(Model model){
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("departments",departmentRepository.findAll());
        return "user/signup";
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute UserDto userDto, RedirectAttributes redirectAttributes){
        try {
            userService.create(userDto);
            redirectAttributes.addAttribute("message", "회원가입에 성공했습니다! 관리자가 회원가입 승인시 로그인이 가능합니다.");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addAttribute("message", e.getMessage());
            return "redirect:/user/signup";
        }
    }
}
