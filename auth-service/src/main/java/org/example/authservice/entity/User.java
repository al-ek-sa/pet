package org.example.commonmodule.dto.entity;

import java.util.UUID;

@Entity
public class User {
    @Id
    private UUID id;
    private String login;
    private String password;
    private String userName;
    private String email;
}
