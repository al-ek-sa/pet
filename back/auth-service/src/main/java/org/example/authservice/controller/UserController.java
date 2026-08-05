package org.example.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.authservice.entity.User;
import org.example.authservice.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public String addUser(@RequestBody User user) {
        userService.save(user);
        return "User added successfully!";
    }
}