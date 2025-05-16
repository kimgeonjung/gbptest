package com.practice.swmbackend.user.dto;

import com.practice.swmbackend.user.Role;
import com.practice.swmbackend.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private Role role;
    private String email;
    private String loginId;
    private String name;
    private String password;

    public User toEntity(){
        return User.builder()
                .role(this.role)
                .email(this.email)
                .loginId(this.loginId)
                .name(this.name)
                .password(this.password)
                .build();
    }
}
