package com.practice.swmbackend.auth;

import com.practice.swmbackend.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    boolean existsByUser(User user);
}
