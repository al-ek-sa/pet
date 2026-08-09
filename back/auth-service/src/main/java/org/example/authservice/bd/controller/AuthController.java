package org.example.authservice.bd.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.bd.service.AuthService;
import org.example.authservice.dto.DtoUser;
import org.example.authservice.dto.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<String> findByLoginAndPassword(@Valid @RequestBody LoginRequest request) {
        log.info("Login attempt for user: {}", request.getLogin());
        String userName = service.findByLoginAndPassword(
                request.getLogin(),
                request.getPassword()
        );
        return ResponseEntity.ok(userName);
    }

    @GetMapping("/check/username")
    public ResponseEntity<Boolean> permissionToUseTheName(
            @RequestParam @NotBlank(message = "Username cannot be empty")
            @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
            String userName) {
        log.info("Checking username availability: {}", userName);
        return ResponseEntity.ok(service.existsByUserName(userName));
    }

    @GetMapping("/check/login")
    public ResponseEntity<Boolean> findLogin(
            @RequestParam @NotBlank(message = "Login cannot be empty")
            @Size(min = 3, max = 30, message = "Login must be between 3 and 30 characters")
            String login) {
        log.info("Checking login availability: {}", login);
        return ResponseEntity.ok(service.existsByLogin(login));
    }

    @PostMapping("/register")
    public ResponseEntity<UUID> save(@Valid @RequestBody DtoUser dtoUser) {
        log.info("Registering new user: {}", dtoUser.getUserName());
        UUID userId = service.saveUser(dtoUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
    }
}