package com.practice.swmbackend.auth.dto;

import com.practice.swmbackend.auth.Auth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    private String tokenType;
    private String accessToken;
    private String refreshToken;

    public static AuthResponseDto from(Auth auth) {
        return new AuthResponseDto(
                auth.getTokenType(),
                auth.getAccessToken(),
                auth.getRefreshToken()
        );
    }
}
