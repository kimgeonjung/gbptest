package com.practice.swmbackend.user;

import com.practice.swmbackend.auth.Auth;
import com.practice.swmbackend.global.BaseTime;
import com.practice.swmbackend.user.dto.UserRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity(name="users")
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 50, nullable = false, unique = true)
    private String loginId;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Auth auth;

    @Builder
    public User(String email, String loginId, String name, String password, Role role) {
        this.role = role;
        this.email = email;
        this.loginId = loginId;
        this.name = name;
        this.password = password;
    }

    public void update(UserRequestDto requestDto) {
        if (requestDto.getRole() != null) {
            this.role = requestDto.getRole();
        }
        if (requestDto.getEmail() != null) {
            this.email = requestDto.getEmail();
        }
        if (requestDto.getLoginId() != null) {
            this.loginId = requestDto.getLoginId();
        }
        if (requestDto.getName() != null) {
            this.name = requestDto.getName();
        }
        if (requestDto.getPassword() != null) {
            this.password = requestDto.getPassword();
        }
    }
}
