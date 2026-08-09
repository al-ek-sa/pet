package org.example.authservice.dto;

import lombok.Data;

@Data
public class DtoUser {
    private String login;
    private String password;
    private String email;
    private String userName;
}
