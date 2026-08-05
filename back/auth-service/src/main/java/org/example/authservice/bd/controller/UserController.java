package org.example.authservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.authservice.entity.User;
import org.example.authservice.bd.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<User> findAllUsers(){
        return userService.findAll();
    }


}