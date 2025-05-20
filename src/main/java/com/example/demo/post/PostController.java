package com.example.demo.post;

import com.example.demo.board.Board;
import com.example.demo.board.BoardRepository;
import com.example.demo.comment.CommentDto;
import com.example.demo.file.FileDto;
import com.example.demo.file.FileService;
import com.example.demo.menus.MenusService;
import com.example.demo.user.entity.SiteUser;
import com.example.demo.user.security.SiteUserDetails;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final FileService fileService;
    private final BoardRepository boardRepository;
    private final MenusService menusService;

    // 게시글 작성 폼
    @GetMapping("/submenu/{subMenuId}/write")
    public String showWriteForm(@PathVariable Long subMenuId, Model model) {
        // 서브메뉴 ID로 Board 조회
        Board board = boardRepository.findBySubMenu_id(subMenuId)
                .orElseThrow(() -> new RuntimeException("해당 서브메뉴에 게시판이 존재하지 않습니다."));

        PostDto postDto = new PostDto();
        postDto.setBoardId(board.getBoardId()); // 진짜 Board의 PK 설정
        model.addAttribute("postForm", postDto);
        model.addAttribute("subMenuId", subMenuId);

        // 메뉴 정보 추가
        model.addAttribute("menus", menusService.findAllMenusWithSubMenus());

        return "board/write";
    }

    // 게시글 작성
    @PostMapping("/submenu/{subMenuId}/create")
    public String createPost(@AuthenticationPrincipal SiteUserDetails userDetails,
                             @ModelAttribute PostDto postDto,
                             @RequestParam(value = "multipartFiles", required = false) List<MultipartFile> multipartFiles,
                             @RequestParam("subMenuId") Long subMenuId) {
        // 게시글 생성
        Post post = postService.createPost(userDetails, postDto);

        // 파일 저장 호출
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            for (MultipartFile file : multipartFiles) {
                try {
                    // 여기서 SiteUser로 전달
                    fileService.saveFile(file, post, userDetails.getUser());
                } catch (IOException e) {
                    e.printStackTrace(); // 파일 저장 실패 시 예외 처리
                }
            }
        }
        // 게시글 작성 완료 후 해당 게시판으로 이동
        return "redirect:/boards/submenu/" + subMenuId;
    }

    // 게시글 수정 (form 방식)
    @PostMapping("/{postId}/edit")
    public String updatePost(@PathVariable Long postId,
                             @ModelAttribute PostDto postDto,
                             @RequestParam(value = "deleteFileIds", required = false) List<Long> deleteFileIds,
                             @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        postService.updatePost(postId, postDto, deleteFileIds, userDetails);
        return "redirect:/posts/" + postId;
    }

    // 게시글 수정 폼 요청
    @GetMapping("/{postId}/edit")
    public String showEditForm(@PathVariable Long postId, Model model) {
        PostDto postDto = postService.getPostById(postId);
        List<FileDto> fileDtos = postService.getFilesByPostId(postId);
        model.addAttribute("post", postDto);
        model.addAttribute("files", fileDtos);
        return "board/edit"; //
    }

    // 게시글 삭제 (form 방식)
    @PostMapping("/{postId}/delete")
    public String deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return "redirect:/home";
    }

    // 게시글 상세 보기 (HTML 렌더링 방식)
    @GetMapping("/{postId}")
    public String viewPost(@PathVariable Long postId,
                           Model model,
                           @AuthenticationPrincipal SiteUserDetails siteUserDetails) {
        PostDto postDto = postService.getPostById(postId);
        List<FileDto> fileDtos = postService.getFilesByPostId(postId);
        List<CommentDto> commentDtos = postService.getCommentsByPostId(postId);

        model.addAttribute("post", postDto);
        model.addAttribute("files", fileDtos);
        model.addAttribute("comments", commentDtos);

        // 메뉴 정보 추가
        model.addAttribute("menus", menusService.findAllMenusWithSubMenus());

        // --- canWrite 계산 ---
        boolean canWrite = false;
        if (siteUserDetails != null) {
            SiteUser loginUser = siteUserDetails.getUser();
            Long subMenuId = postDto.getSubMenuId(); // PostDto에 있어야 함

            // 권한 체크 PostService에 위임
            canWrite = postService.canUserWriteToSubMenu(loginUser, subMenuId);
        }
        model.addAttribute("canWrite", canWrite);

        return "board/view";
    }
}
