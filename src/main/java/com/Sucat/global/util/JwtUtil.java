package com.Sucat.global.util;

import com.Sucat.domain.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface JwtUtil {
    String createAccessToken(String email, String role); // AccessToken 생성
    String createRefreshToken(String email, String role); // RefreshToken 생성 - 보안, I/O 감소 이유로 사용

    String createAdminAccessToken(String email, String role);

    void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken); // Token들 전송
    void sendAccessToken(HttpServletResponse response, String accessToken); // AccessToken 전송

    Optional<String> extractAccessToken(HttpServletRequest request);

    Optional<String> extractRefreshToken(HttpServletRequest request);

    String extractEmail(String accessToken);
    String extractRole(String accessToken);

    void setAccessTokenHeader(HttpServletResponse response, String accessToken);

    void setRefreshTokenHeader(HttpServletResponse response, String refreshToken);

    boolean isTokenValid(String token);

    User getUserFromRequest(HttpServletRequest request);

    String getEmailFromRequest(HttpServletRequest request);
}
