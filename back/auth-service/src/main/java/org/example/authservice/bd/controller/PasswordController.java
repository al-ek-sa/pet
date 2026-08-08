package org.example.authservice.bd.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.bd.service.PasswordService;
import org.example.authservice.dto.LoginRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
@Slf4j
public class PasswordController {

    private final PasswordService service;

    @PostMapping("/change")
    public boolean updatePassword(@RequestBody LoginRequest loginRequest){
        return service.updatePassword(
                loginRequest.getLogin(),
                loginRequest.getPassword()) != 0;
    }
}
