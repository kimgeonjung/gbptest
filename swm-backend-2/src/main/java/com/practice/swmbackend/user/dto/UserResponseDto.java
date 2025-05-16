package com.practice.swmbackend.user.dto;

import com.practice.swmbackend.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String role;
    private String email;
    private String loginId;
    private String name;

    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getRole().name(),
                user.getEmail(),
                user.getLoginId(),
                user.getName()
        );
    }
}
