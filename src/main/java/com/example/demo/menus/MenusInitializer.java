package com.example.demo.menus;

import com.example.demo.department.Department;
import com.example.demo.department.DepartmentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MenusInitializer {

    private final MenusRepository menusRepository;

    @PostConstruct
    public void initMenus() {
        try {
            System.out.println("MenusInitializer 실행됨!");

            if (menusRepository.count() == 0) {
                List<String> menuNames = Arrays.asList(
                        "현장소개", "건축", "토목", "설비", "안전보건", "공무", "공사일보", "일정관리", "공지사항"
                );

                for (String name : menuNames) {
                    System.out.println("메뉴 추가 : " + name);
                    menusRepository.save(new Menus(name));
                }

                System.out.println("Menus 초기화 완료!");

            }

            menusRepository.findAll().forEach(m -> System.out.println("DB 메뉴: " + m.getName()));

        } catch (Exception e) {
            System.err.println("초기화 중 오류 발생 : " + e.getMessage());
            e.printStackTrace();
        }

    }
}
