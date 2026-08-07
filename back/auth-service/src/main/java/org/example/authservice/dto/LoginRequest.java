package org.example.authservice.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String login;
    private String password;
}