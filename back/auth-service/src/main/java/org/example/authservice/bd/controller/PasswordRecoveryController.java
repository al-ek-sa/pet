package org.example.authservice.bd.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.bd.service.PasswordRecoveryService;
import org.example.authservice.dto.LoginRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recovery")
@RequiredArgsConstructor
@Slf4j
public class PasswordRecoveryController {

    private final PasswordRecoveryService passwordRecoveryService;

    @PostMapping
    public boolean updatePassword(@RequestBody LoginRequest loginRequest){
        return passwordRecoveryService.updatePassword(
                loginRequest.getLogin(),
                loginRequest.getPassword()) != 0;
    }
}
