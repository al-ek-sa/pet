package org.example.authservice.bd.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.bd.service.LoginUserService;
import org.example.authservice.dto.LoginRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
@Slf4j
public class LoginUserController {
    private final LoginUserService loginUserService;

    @PostMapping("/login")
    public String findByLoginAndPassword(@RequestBody LoginRequest request) {
        return loginUserService.findByLoginAndPassword(
                request.getLogin(),
                request.getPassword()
        );
    }
}
