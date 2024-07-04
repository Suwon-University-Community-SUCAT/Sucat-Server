package com.Sucat.domain.user.service;

import com.Sucat.domain.user.exception.UserException;
import com.Sucat.domain.user.model.User;
import com.Sucat.domain.user.repository.UserRepository;
import com.Sucat.global.common.code.ErrorCode;
import com.Sucat.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.Sucat.domain.user.dto.UserDto.PasswordResetRequest;
import static com.Sucat.global.common.constant.ConstraintConstants.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 비밀번호 암호화 메서드
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    public void emailDuplicateVerification(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserException(ErrorCode.USER_ALREADY_EXISTS);
        }
    }

    public void nicknameDuplicateVerification(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent())  {
            throw new UserException(ErrorCode.NICKNAME_DUPLICATION);
        }
    }

    public User getUserInfo(HttpServletRequest request) {
        return jwtUtil.getUserFromRequest(request);
    }

    @Transactional
    public void resetPassword(User currentUser, PasswordResetRequest passwordResetRequest) {
        String currentUserEmail = currentUser.getEmail();

        if (!currentUserEmail.equals(passwordResetRequest.email())) {
            throw new UserException(ErrorCode.USER_MISMATCH);
        }

//        currentUser.resetPassword(passwordEncoder.encode(passwordResetRequest.password()));
        String resetPassword = passwordResetRequest.password();
        validatePassword(resetPassword);
        currentUser.resetPassword(passwordEncoder.encode(resetPassword));

    }

    // 비밀번호 유효성 검사 메서드
    public void validatePassword(String password) {
        // 비밀번호 만료 날짜 설정, 이전 비밀번호와의 비교 등 정책 추가 고민
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 null이나 빈 문자열일 수 없습니다.");
        }
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("비밀번호는 최소 " + MIN_PASSWORD_LENGTH + "자에서 최대 " + MAX_PASSWORD_LENGTH + "자여야 합니다.");
        }
        if (!password.matches(PASSWORD_PATTERN)) {
            throw new IllegalArgumentException("비밀번호는 숫자, 문자, 특수문자를 모두 포함해야 합니다.");
        }
    }
}
