package com.practice.swmbackend.auth;

import com.practice.swmbackend.global.BaseTime;
import com.practice.swmbackend.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Auth extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String tokenType;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user-id")
    private User user;

    @Builder
    public Auth(User user, String tokenType, String accessToken, String refreshToken) {
        this.user = user;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void updateAccessToken(String newAccessToken) {
        this.accessToken = newAccessToken;
    }

    public void updateRefreshToken(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
    }
}
