package com.practice.swmbackend.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    private String value;
}
