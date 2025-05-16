package com.practice.swmbackend.user;

public enum Role {
    USER, ADMIN;

    public String getRole(){
        return "ROLE_" + name();
    }
}
