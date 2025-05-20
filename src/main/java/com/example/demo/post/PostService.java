package com.example.demo.post;

import com.example.demo.board.Board;
import com.example.demo.board.BoardRepository;
import com.example.demo.comment.Comment;
import com.example.demo.comment.CommentDto;
import com.example.demo.comment.CommentRepository;
import com.example.demo.department.Department;
import com.example.demo.file.File;
import com.example.demo.file.FileDto;
import com.example.demo.file.FileRepository;
import com.example.demo.file.FileService;
import com.example.demo.submenus.SubMenus;
import com.example.demo.submenus.SubMenusRepository;
import com.example.demo.user.entity.SiteUser;
import com.example.demo.user.model.Role;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.user.security.SiteUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FileRepository fileRepository;
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final SubMenusRepository subMenusRepository;
    private final FileService fileService; // 파일 저장 로직 담당 서비스
    private final UserRepository userRepository;

    // 게시글 상세 조회
    public PostDto getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return PostDto.fromEntity(post);
    }

    // 게시글에 첨부된 파일 목록
    public List<FileDto> getFilesByPostId(Long postId) {
        List<File> files = fileRepository.findByPost_PostId(postId);
        return files.stream()
                .map(FileDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 게시글에 대한 댓글 목록
    public List<CommentDto> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPost_PostId(postId);
        return comments.stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 게시글 작성
    public Post createPost(SiteUserDetails userDetails, PostDto postDto) {
        // 게시판 ID로 게시판 조회
        Board board = boardRepository.findById(postDto.getBoardId())
                .orElseThrow(() -> new RuntimeException("게시판을 찾을 수 없습니다."));
        // 게시글 생성
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setBoard(board);
        post.setUser(userDetails.getUser()); // 작성자 설정

        return postRepository.save(post);
    }

    // 게시글 삭제
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 파일 삭제
        List<File> files = fileRepository.findByPost_PostId(postId);
        for (File file : files) {
            deleteFileFromDisk(file.getFilePath()); // 로컬 저장소에서 삭제
            fileRepository.delete(file); // DB에서 삭제
        }

        postRepository.delete(post);
    }

    private void deleteFileFromDisk(String path) {
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace(); // 필요시 로그 남기기
        }
    }

    @Transactional
    public void updatePost(Long postId, PostDto postDto, List<Long> deleteFileIds, UserDetails userDetails) throws IOException {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUpdatedAt(LocalDateTime.now());

        // 삭제할 파일이 있다면 삭제
        if (deleteFileIds != null) {
            for (Long fileId : deleteFileIds) {
                fileService.deleteFile(fileId);
            }
        }

        // 새로 추가한 파일 저장
        List<MultipartFile> multipartFiles = postDto.getMultipartFiles();
        if (multipartFiles != null) {
            SiteUser user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
            for (MultipartFile file : multipartFiles) {
                if (!file.isEmpty()) {
                    fileService.saveFile(file, post, user);
                }
            }
        }
    }

    // 게시글 작성 권한 여부 체크
    @Transactional(readOnly = true)
    public boolean canUserWriteToSubMenu(SiteUser user, Long subMenuId) {
        SubMenus subMenu = subMenusRepository.findById(subMenuId)
                .orElseThrow(() -> new RuntimeException("해당 서브메뉴가 존재하지 않습니다."));

        List<Department> allowedDepartments = subMenu.getUploadDepartments();

        if (allowedDepartments.isEmpty()) {
            // 업로드 허용 부서가 비어있으면 전체 허용으로 간주
            return true;
        }

        Long userDeptId = user.getDepartment().getId();

        // contains 대신 ID 비교 사용
        return allowedDepartments.stream()
                .anyMatch(dept -> dept.getId().equals(userDeptId));
    }
}
