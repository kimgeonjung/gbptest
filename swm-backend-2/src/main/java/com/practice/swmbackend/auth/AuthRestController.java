package com.practice.swmbackend.auth;

import com.practice.swmbackend.auth.dto.AuthRequestDto;
import com.practice.swmbackend.auth.dto.AuthResponseDto;
import com.practice.swmbackend.user.UserService;
import com.practice.swmbackend.user.dto.UserRequestDto;
import com.practice.swmbackend.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthRestController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDto requestDto){
        AuthResponseDto responseDto = authService.login(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody UserRequestDto requestDto){
        UserResponseDto responseDto = userService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestHeader("REFRESH_TOKEN") String refreshToken){
        String newAccessToken = authService.reissue(refreshToken).getAccessToken();
        return ResponseEntity.status(HttpStatus.OK).body(newAccessToken);
    }
}
