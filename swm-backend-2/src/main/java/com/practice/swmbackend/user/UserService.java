package com.practice.swmbackend.user;

import com.practice.swmbackend.user.dto.UserRequestDto;
import com.practice.swmbackend.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // user 조회
    @Transactional
    public UserResponseDto findByLoginId(String loginId) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다: "+ loginId));
        return UserResponseDto.from(user);
    }

    // user 수정
    @Transactional
    public void update(String loginId, UserRequestDto userRequestDto) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다: "+ loginId));
        user.update(userRequestDto);
    }

    // user 삭제
    @Transactional
    public void delete(String loginId) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다: "+ loginId));
        userRepository.delete(user);
    }

    // 회원 가입
    @Transactional
    public UserResponseDto signup(UserRequestDto requestDto) {
        requestDto.setRole(Role.ROLE_USER);
        requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        User savedUser = userRepository.save(requestDto.toEntity());
        
        return UserResponseDto.from(savedUser);
    }
}
