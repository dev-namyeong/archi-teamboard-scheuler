package com.example.demo.admin;

import com.example.demo.user.entity.SiteUser;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    // 승인되지 않은 사용자 목록 가져오기
    public List<SiteUser> getUnapprovedUsers(){
        return userRepository.findByIsApprovedFalse();//
    }

    // 사용자 승인 처리
    public void approveUser(long userId){
        SiteUser user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("해당 사용자를 찾을 수 없습니다."));
        user.setIsApproved(true);
        userRepository.save(user);
    }
}
