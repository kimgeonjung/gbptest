package com.practice.swmbackend.auth;

import com.practice.swmbackend.auth.dto.LoginRequestDto;
import com.practice.swmbackend.auth.dto.TokenResponse;
import com.practice.swmbackend.global.exception.JwtAuthException;
import com.practice.swmbackend.global.exception.LoginFailedException;
import com.practice.swmbackend.user.Users;
import com.practice.swmbackend.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UsersRepository usersRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenResponse login(LoginRequestDto request) {
        // 사용자 확인
        Users user = usersRepository.findByLoginId(request.getLoginId())
                .orElseThrow(()-> new LoginFailedException("아이디 또는 비밀번호가 틀렸습니다"));
        // 비밀번호 확인
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new LoginFailedException("아이디 또는 비밀번호가 틀렸습니다");
        }
        // 토큰 발급
        String accessToken = jwtProvider.generateAccessToken(user.getLoginId());
        String refreshToken = jwtProvider.generateRefreshToken(user.getLoginId());

        // DB 에 refresh 저장(덮어쓰기)
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .loginId(user.getLoginId())
                        .token(refreshToken)
                        .build()
        );

        return new TokenResponse(accessToken, refreshToken);
    }

    @Transactional
    public TokenResponse reissue(String refreshToken) {
        // 1. 리프레시 토큰 유효성 검사
        if(!jwtProvider.validateToken(refreshToken)){
            throw new JwtAuthException("유효하지 않은 리프레시 토큰입니다.");
        }

        // 2. 토큰에서 loginId(subject) 추출
        String loginId = jwtProvider.getSubject(refreshToken);

        // 3. DB 에 저장된 리프레시 토큰과 일치하는지 확인(보안)
        RefreshToken savedToken = refreshTokenRepository.findByLoginId(loginId)
                .orElseThrow(() -> new JwtAuthException("저장된 리프레시 토큰이 없습니다."));

        if(!savedToken.getToken().equals(refreshToken)){
            throw new JwtAuthException("리프레시 토큰이 일치하지 않습니다.");
        }

        // 4. 유저 정보 재조회(권한 포함)
        Users user = usersRepository.findByLoginId(loginId)
                .orElseThrow(() -> new JwtAuthException("해당 유저를 찾을 수 없습니다."));

        // 5. 새 token 발급
        String newAccessToken = jwtProvider.generateAccessToken(user.getLoginId());
        String newRefreshToken = jwtProvider.generateRefreshToken(user.getLoginId());

        savedToken.updateToken(newRefreshToken);
        refreshTokenRepository.save(savedToken);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(String accessToken) {
        if(!jwtProvider.validateToken(accessToken)){
            throw new JwtAuthException("유효하지 않은 토큰입니다.");
        }

        String loginId = jwtProvider.getSubject(accessToken);

        refreshTokenRepository.deleteByLoginId(loginId);
    }
}
