package com.example.librarymgmt.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    private final String token;
    private final String type = "Bearer";
    private final String username;
    private final String role;

    public LoginResponse(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
    }
}
