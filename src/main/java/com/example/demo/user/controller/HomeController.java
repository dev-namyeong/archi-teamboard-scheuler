package com.example.demo.user.controller;

import com.example.demo.menus.Menus;
import com.example.demo.menus.MenusService;
import com.example.demo.post.Post;
import com.example.demo.post.PostRepository;
import com.example.demo.submenus.SubMenus;
import com.example.demo.submenus.SubMenusRepository;
import com.example.demo.user.entity.SiteUser;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController extends BaseController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SubMenusRepository subMenusRepository;

    // 생성자에서 메뉴 서비스와 하위 메뉴 리포지토리 전달
    public HomeController(MenusService menusService,
                          PostRepository postRepository,
                          UserRepository userRepository,
                          SubMenusRepository subMenusRepository) {
        super(menusService, subMenusRepository); // BaseController 생성자 호출
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.subMenusRepository = subMenusRepository;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/home")
    public String homePage(Model model, Principal principal) {
        // 상단 메뉴와 하위 메뉴 정보 추가
        addMenusToModel(model);

        // Principal 객체에서 아이디(username)을 가져옴
        String username = principal.getName();

        // username을 이용해 SiteUser를 조회
        SiteUser siteUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // SiteUser의 이름(name)을 model에 추가
        model.addAttribute("name", siteUser.getName());

        // 공지사항 게시판의 최근 글 5개 가져오기
        List<Post> noticePosts = postRepository.findTop5ByBoard_SubMenu_NameOrderByCreatedAtDesc("공지사항 게시판");
        model.addAttribute("notices", noticePosts);

        // 공지사항 SubMenu의 id도 같이 넘기기 (더보기 링크용)
        Optional<SubMenus> noticeSubMenuOpt = subMenusRepository.findByName("공지사항 게시판");
        noticeSubMenuOpt.ifPresent(noticeSubMenu -> model.addAttribute("noticeSubMenuId", noticeSubMenu.getId()));

        return "home";
    }
}