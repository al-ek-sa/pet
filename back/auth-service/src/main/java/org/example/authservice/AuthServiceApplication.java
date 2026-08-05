package org.example.authservice;

import org.example.authservice.entity.User;
import org.example.authservice.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AuthServiceApplication.class, args);
        UserService userService = context.getBean(UserService.class);
        User user = User.builder()
                .login("alexa1")
                .password("password1231")
                .userName("Aliaksandra1")
                .email("alid@gmail.com")
                .active(true)
                .build();
        userService.save(user);

        System.out.println(userService.findAll());
    }
}