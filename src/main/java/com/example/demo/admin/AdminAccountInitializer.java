package com.example.demo.admin;

import com.example.demo.department.Department;
import com.example.demo.department.DepartmentRepository;
import com.example.demo.user.entity.SiteUser;
import com.example.demo.user.model.Role;
import com.example.demo.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@DependsOn("departmentInitializer")
@RequiredArgsConstructor
public class AdminAccountInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;

    @PostConstruct
    public void initAdmin() {
        if (!userRepository.existsByUsername("admin")) {
            SiteUser admin = new SiteUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setName("관리자");
            admin.setPhoneNumber("010-0000-0000");
            admin.setPosition("관리자");

            System.out.println("==부서 조회 시작==");
            Department defaultDept = departmentRepository.findByName("안전보건")
                    .orElseThrow(() -> new RuntimeException("11111"));
            admin.setDepartment(defaultDept);

            admin.setIsApproved(true);
            admin.setRole(Role.ROLE_ADMIN);

            userRepository.save(admin);
            System.out.println("== 관리자 계정 생성 완료 ==");
        }
    }
}


