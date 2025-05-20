package com.example.demo.user.security;

import com.example.demo.user.entity.SiteUser;
import com.example.demo.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final UserRepository userRepository;

    public CustomAuthenticationFailureHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String username = request.getParameter("username");
        Optional<SiteUser> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            SiteUser user = userOptional.get();
            if (!user.getIsApproved()) { // 승인 여부 체크
                response.sendRedirect("/?error=notApproved");
                return;
            }
        }

        response.sendRedirect("/?error=invalid");
    }
}
