package com.practice.swmbackend.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(username).orElseThrow(
                () -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다: " + username));
        return new CustomUserDetails(user);
    }

    // 필요시 이메일, ID를 통해 user 를 찾는 기능을 추가할 수 있음. 근데 안해도 되게 만드는 중
}
