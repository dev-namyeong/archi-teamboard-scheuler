package com.example.demo.user.repository;

import com.example.demo.user.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByUsername(String username);

    boolean existsByUsername(String username);

    // 회원가입 승인 되지않은 사용자 목록 조회
    List<SiteUser> findByIsApprovedFalse();

}
