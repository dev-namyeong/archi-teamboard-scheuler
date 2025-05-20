package com.example.demo.submenus;

import com.example.demo.board.Board;
import com.example.demo.board.BoardRepository;
import com.example.demo.department.Department;
import com.example.demo.department.DepartmentRepository;
import com.example.demo.menus.Menus;
import com.example.demo.menus.MenusRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubMenusInitializer {

    private final MenusRepository menusRepository;
    private final SubMenusRepository subMenusRepository;
    private final BoardRepository boardRepository;
    private final DepartmentRepository departmentRepository;

    @PostConstruct
    public void initSubMenus() {
        List<Department> allDepartments = departmentRepository.findAll();

        // 1. 일정관리 메뉴 처리
        Optional<Menus> scheduleMenuOpt = menusRepository.findByName("일정관리");
        if (scheduleMenuOpt.isPresent()) {
            Menus schedulMenu = scheduleMenuOpt.get();

            if (subMenusRepository.findByMenu(schedulMenu).isEmpty()) {
                SubMenus calendar = new SubMenus(
                        schedulMenu,
                        "연차캘린더",
                        false,
                        allDepartments,
                        null
                );

                SubMenus form = new SubMenus(
                        schedulMenu,
                        "일정관리양식",
                        true,
                        allDepartments,
                        null
                );

                subMenusRepository.save(calendar);
                subMenusRepository.save(form);

                Board board = new Board();
                board.setSubMenu(form);
                board.setName("일정관리양식 게시판");
                boardRepository.save(board);

                System.out.println("일정관리 메뉴의 SubMenus 초기화 완료!");
            }
        } else {
            System.err.println("'일정관리' 메뉴를 찾을 수 없습니다. MenusInitializer가 먼저 실행되었는지 확인하세요.");
        }

        // 2. 공지사항 메뉴 처리
        Optional<Menus> noticeMenuOpt = menusRepository.findByName("공지사항");
        if (noticeMenuOpt.isPresent()) {
            Menus noticeMenu = noticeMenuOpt.get();

            if (subMenusRepository.findByMenu(noticeMenu).isEmpty()) {
                SubMenus noticeSubMenu = new SubMenus(
                        noticeMenu,
                        "공지사항 게시판",
                        true,
                        allDepartments,
                        null
                );

                subMenusRepository.save(noticeSubMenu);

                Board noticeBoard = new Board();
                noticeBoard.setSubMenu(noticeSubMenu);
                noticeBoard.setName("공지사항 게시판");
                boardRepository.save(noticeBoard);

                System.out.println("공지사항 메뉴의 SubMenus 초기화 완료!");
            }
        } else {
            System.err.println("'공지사항' 메뉴를 찾을 수 없습니다. MenusInitializer가 먼저 실행되었는지 확인하세요.");
        }

        // 3. 현장소개 메뉴 처리
        Optional<Menus> siteOverviewMenuOpt = menusRepository.findByName("현장소개");
        if (siteOverviewMenuOpt.isPresent()) {
            Menus siteOverviewMenu = siteOverviewMenuOpt.get();
            Long menuId = siteOverviewMenu.getId();

            if (subMenusRepository.findByMenuIdAndName(menuId, "현장개요").isEmpty()) {
                SubMenus overview = new SubMenus(
                        siteOverviewMenu,
                        "현장개요",
                        false,
                        allDepartments,
                        "site/overview"
                );
                subMenusRepository.save(overview);
                System.out.println("현장개요 SubMenu 추가됨!");
            }

            if (subMenusRepository.findByMenuIdAndName(menuId, "조직도").isEmpty()) {
                SubMenus org = new SubMenus(
                        siteOverviewMenu,
                        "조직도",
                        false,
                        allDepartments,
                        "site/organization"
                );
                subMenusRepository.save(org);
                System.out.println("조직도 SubMenu 추가됨!");
            }

        } else {
            System.err.println("'현장소개' 메뉴를 찾을 수 없습니다.");
        }
    }
}