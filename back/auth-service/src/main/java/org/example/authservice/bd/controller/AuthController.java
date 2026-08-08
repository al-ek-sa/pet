package org.example.authservice.bd.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.bd.entity.User;
import org.example.authservice.bd.service.AuthService;
import org.example.authservice.dto.LoginRequest;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public String findByLoginAndPassword(@RequestBody LoginRequest request) {
        return service.findByLoginAndPassword(
                request.getLogin(),
                request.getPassword()
        );
    }

    @GetMapping("/check/username")
    public boolean permissionToUseTheName(String userName){
        return service.existsByUserName(userName);
    }

    @GetMapping("/check/login")
    public boolean findLogin(String login){
        return service.existsByLogin(login);
    }

    @PostMapping("/register")
    public UUID save(@RequestBody User user){
        return service.saveUser(user);
    }
}
