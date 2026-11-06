package com.launchpoint.auth;

public class AuthResponse {

    private String message;
    private Long userId;
    private String email;
    private String role;

    public AuthResponse(String message, Long userId, String email, String role) {
        this.message = message;
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}