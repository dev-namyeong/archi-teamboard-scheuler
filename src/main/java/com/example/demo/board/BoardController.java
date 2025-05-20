package com.example.demo.board;

import com.example.demo.department.Department;
import com.example.demo.menus.MenusService;
import com.example.demo.post.Post;
import com.example.demo.post.PostDto;
import com.example.demo.post.PostRepository;
import com.example.demo.post.PostService;
import com.example.demo.user.entity.SiteUser;
import com.example.demo.user.model.Role;
import com.example.demo.user.security.SiteUserDetails;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final PostService postService;
    private final UserService userService;
    private final MenusService menusService;

    @GetMapping("/submenu/{subMenuId}")
    public String viewBoardBySubMenu(@PathVariable Long subMenuId,
                                     Model model,
                                     Principal principal) {
        Board board = boardService.getBoardBySubMenuId(subMenuId);
        List<PostDto> postDtos = boardService.getPostsBySubMenu(subMenuId);

        model.addAttribute("board", board);
        model.addAttribute("boardName", board.getName());
        model.addAttribute("posts", postDtos);

        // 메뉴 정보 추가
        model.addAttribute("menus", menusService.findAllMenusWithSubMenus());

        boolean hasUploadPermission = false;

        if (principal != null) {
            String username = principal.getName();
            SiteUser user = userService.findByUsername(username);

            // 권한 체크를 PostService에 위임
            hasUploadPermission = postService.canUserWriteToSubMenu(user, board.getSubMenu().getId());
        }

        model.addAttribute("hasUploadPermission", hasUploadPermission);

        model.addAttribute("subMenuId", subMenuId);
        return "board/list";
    }

}
