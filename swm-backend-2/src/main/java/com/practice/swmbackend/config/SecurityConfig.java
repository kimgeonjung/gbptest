package com.practice.swmbackend.config;

import com.practice.swmbackend.auth.jwt.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // 관리자 관련 모든 요청에 대해 ADMIN 권한이 있는 사용자만 허용
                        .requestMatchers("/api/v1/admin/**", "/api/v2/admin/**").hasRole("ADMIN")
                        // 회원가입 및 로그인 관련 모든 요청에 대해 아무나 승인
                        .requestMatchers("/api/v1/auth/**", "/api/v2/auth/**").permitAll()
                        // 중복체크 관련 모든 요청에 대해 아무나 허용
                        .requestMatchers("/api/v1/user/check/**", "/api/v2/user/check/**").permitAll()
                        // 유저정보 관련 모든 요청에 대해 승인된 사용자만 허용
                        .requestMatchers("/api/v1/user/**", "/api/v2/user/**").authenticated()
                        // 첨부파일 관련 GET 요청에 대해 아무나 승인
                        .requestMatchers(HttpMethod.GET, "/api/v1/attachment/**", "/api/v2/attachment/**").permitAll()
                        // 댓글 관련 GET 요청에 대해 아무나 승인
                        .requestMatchers(HttpMethod.GET, "/api/v1/comment/**", "/api/v2/comment/**").permitAll()
                        // 게시글 관련 GET 요청에 대해 아무나 승인
                        .requestMatchers(HttpMethod.GET, "/api/v1/post/**", "/api/v2/post/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        // 기타 모든 요청에 대해 승인된 사용자만 허용
                        .requestMatchers("/api/v1/**", "/api/v2/**").authenticated()
                )
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
