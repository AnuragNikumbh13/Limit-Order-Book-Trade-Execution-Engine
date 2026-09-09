package com.anurag.hft.dto;

public class LoginResponseDTO {
    private String message;
    private String username;
    private String token;

    public LoginResponseDTO(String message, String username, String token) {
        this.message = message;
        this.username = username;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginResponseDTO{" +
                "message='" + message + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
