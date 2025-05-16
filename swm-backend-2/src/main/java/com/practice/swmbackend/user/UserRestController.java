package com.practice.swmbackend.user;

import com.practice.swmbackend.auth.jwt.JwtProvider;
import com.practice.swmbackend.user.dto.UserRequestDto;
import com.practice.swmbackend.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserRestController {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    // 조회
    @GetMapping
    public ResponseEntity<?> findUser(@RequestHeader("Authorization") String accessToken) {
        String username = jwtProvider.getUsernameFromToken(accessToken.substring(7));
        UserResponseDto responseDto = userService.findByLoginId(username);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 수정
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String accessToken,
                                        @RequestBody UserRequestDto requestDto) {
        String username = jwtProvider.getUsernameFromToken(accessToken.substring(7));
        userService.update(username, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    // 삭제
    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String accessToken) {
        String username = jwtProvider.getUsernameFromToken(accessToken.substring(7));
        userService.delete(username);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
