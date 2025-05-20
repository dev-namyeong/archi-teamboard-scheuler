package com.example.demo.user.dto;

import com.example.demo.user.entity.SiteUser;
import com.example.demo.user.model.Role;
import com.example.demo.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
public class UserDto {

    @NotBlank(message = "아이디는 필수입니다.")
    @Size(min = 3, max = 20, message = "아이디는 3자 이상 20자 이하로 입력하세요.")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 3, max = 20, message = "비밀번호는 최소 3자 이상이어야 합니다.")
    private String password;

    private String name;

    private String position;

    private String phoneNumber;

    @NotNull(message = "부서를 선택해주세요.")
    private Long departmentId; // 부서 ID
}
