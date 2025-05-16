package com.practice.swmbackend.auth;

import com.practice.swmbackend.auth.dto.LoginRequestDto;
import com.practice.swmbackend.auth.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequestDto request) {
        return authService.login(request);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestHeader("Authorization") String bearerToken) {
        String refreshToken = bearerToken.replace("Bearer ", "");
        TokenResponse response = authService.reissue(refreshToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");
        authService.logout(token);
        return ResponseEntity.ok("로그아웃이 완료되었습니다.");
    }
}
