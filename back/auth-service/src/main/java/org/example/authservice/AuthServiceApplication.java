package org.example.authservice;

import org.example.authservice.entity.User;
import org.example.authservice.gmail.servise.AuthService;
import org.example.authservice.bd.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AuthServiceApplication.class, args);
        UserService userService = context.getBean(UserService.class);
        User user = User.builder()
                .login("alex32a1adsd1")
                .password("password12233ad2ad1")
                .userName("Aliaks234aaddndras2")
                .email("lishik135@gmail.com")
                .active(true)
                .build();
        AuthService authService = context.getBean(AuthService.class);
        authService.registerUser(user);
    }
}