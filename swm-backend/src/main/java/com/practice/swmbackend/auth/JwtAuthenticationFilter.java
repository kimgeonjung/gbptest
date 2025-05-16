package com.practice.swmbackend.auth;

import com.practice.swmbackend.user.CustomUserDetail;
import com.practice.swmbackend.user.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. HTTP 요청에서 토큰 추출
        String token = jwtProvider.resolveToken(request);

        // 2. 토큰이 유효하면 인증 정보를 SecurityContext에 저장
        if(token != null && jwtProvider.validateToken(token)) {
            Claims claims = jwtProvider.getClaims(token);
            String loginId = claims.getSubject(); // 로그인 id (subject)

            // 인증 객체를 생성해서 SecurityContext에 세팅
            // 여기서 인증된 사용자 객체를 SecurityContext에 저장
            CustomUserDetail userDetail = (CustomUserDetail) userDetailsService.loadUserByUsername(loginId);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 3. 필터 체인에 요청 넘김
        filterChain.doFilter(request, response);
    }
}
