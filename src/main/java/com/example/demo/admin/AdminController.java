package com.example.demo.admin;

import com.example.demo.user.entity.SiteUser;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // 이 컨트롤러의 모든 메서드는 관리자만 접근 가능!
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 관리자 메인 대시보드
    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard"; // 단순 링크 모음 페이지
    }

    // 승인 대기중인 회원 목록 보기
    @GetMapping("/users")
    public String viewPendingUsers(Model model) {
        //승인 대기 중인 사용자 목록을 가져와서 모델에 추가
        List<SiteUser> pendingUsers = adminService.getUnapprovedUsers();
        model.addAttribute("pendingUsers", pendingUsers);
        return "admin/users";
    }

    @PostMapping("/approve")
    public String approveUser(@RequestParam("userId") long userId) {
        adminService.approveUser(userId); // 해당 사용자 승인 처리
        return "redirect:/admin/users";
    }

}
