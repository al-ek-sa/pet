package org.example.authservice.dto;

import lombok.Data;

@Data
public class EmailDtoTwo {
    private String login;
    private String code;
    private String email;
    private String html;
}
