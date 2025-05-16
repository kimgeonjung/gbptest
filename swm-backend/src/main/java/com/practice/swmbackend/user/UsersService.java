package com.practice.swmbackend.user;

import com.practice.swmbackend.global.exception.SignUpFailedException;
import com.practice.swmbackend.user.dto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUpRequestDto request){
        if(usersRepository.findByLoginId(request.getLoginId()).isPresent()){
            throw  new SignUpFailedException(request.getLoginId()+" 이미 존재하는 아이디입니다.");
        }

        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);

        Users user = Users.builder()
                .loginId(request.getLoginId())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();

        usersRepository.save(user);
    }
}
