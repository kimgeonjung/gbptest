package com.practice.swmbackend.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Users user = usersRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException(loginId+" 사용자를 찾을 수 없습니다."));

        return new CustomUserDetail(user);
    }
}
