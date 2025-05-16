package com.practice.swmbackend.auth;

import com.practice.swmbackend.auth.dto.AuthRequestDto;
import com.practice.swmbackend.auth.dto.AuthResponseDto;
import com.practice.swmbackend.auth.dto.TokenResponse;
import com.practice.swmbackend.auth.jwt.JwtProvider;
import com.practice.swmbackend.auth.jwt.RefreshToken;
import com.practice.swmbackend.auth.jwt.RefreshTokenRepository;
import com.practice.swmbackend.global.exception.JwtAuthException;
import com.practice.swmbackend.global.exception.LoginFailedException;
import com.practice.swmbackend.user.CustomUserDetails;
import com.practice.swmbackend.user.CustomUserDetailsService;
import com.practice.swmbackend.user.User;
import com.practice.swmbackend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    // 로그인
    @Transactional
    public AuthResponseDto login(AuthRequestDto requestDto) {
        User user = userRepository.findByLoginId(requestDto.getLoginId()).orElseThrow(
                () -> new LoginFailedException("아이디 또는 비밀번호가 일치하지 않습니다."));
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new LoginFailedException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        CustomUserDetails userDetail = (CustomUserDetails) customUserDetailsService.loadUserByUsername(user.getLoginId());

        String accessToken = jwtProvider.generateAccessToken(
                new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities()));
        String refreshToken = jwtProvider.generateRefreshToken(
                new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities()));

        if(authRepository.existsByUser(user)){
            user.getAuth().updateAccessToken(accessToken);
            user.getAuth().updateRefreshToken(refreshToken);
            return AuthResponseDto.from(user.getAuth());
        }

        Auth auth = authRepository.save(Auth.builder()
                .user(user)
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
        return AuthResponseDto.from(auth);
    }

    // 토큰 갱신
    @Transactional
    public TokenResponse reissue(String refreshToken) {
        // 1. 리프레시 토큰 유효성 검사
        if(!jwtProvider.validateToken(refreshToken)){
            throw new JwtAuthException("유효하지 않은 리프레시 토큰입니다.");
        }

        // 2. 토큰에서 loginId(subject) 추출
        String loginId = jwtProvider.getUsernameFromToken(refreshToken);

        // 3. DB 에 저장된 리프레시 토큰과 일치하는지 확인(보안)
        RefreshToken savedToken = refreshTokenRepository.findByLoginId(loginId)
                .orElseThrow(() -> new JwtAuthException("저장된 리프레시 토큰이 없습니다."));

        if(!savedToken.getToken().equals(refreshToken)){
            throw new JwtAuthException("리프레시 토큰이 일치하지 않습니다.");
        }

        // 4. 유저 정보 재조회(권한 포함)
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new JwtAuthException("해당 유저를 찾을 수 없습니다."));
        CustomUserDetails userDetail = (CustomUserDetails) customUserDetailsService.loadUserByUsername(user.getLoginId());

        // 5. 새 token 발급
        String newAccessToken = jwtProvider.generateAccessToken(
                new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities()));
        String newRefreshToken = jwtProvider.generateRefreshToken(
                new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities()));

        savedToken.updateToken(newRefreshToken);
        refreshTokenRepository.save(savedToken);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
