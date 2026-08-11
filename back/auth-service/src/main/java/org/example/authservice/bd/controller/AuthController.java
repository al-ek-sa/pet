package org.example.authservice.bd.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.dto.AuthResponse;
import org.example.authservice.dto.DtoUser;
import org.example.authservice.dto.LoginRequest;
import org.example.authservice.jwt.AuthServiceJwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthController {

    private final AuthServiceJwt authService;

    /**
     * Аутентификация пользователя и получение JWT токенов
     *
     * @param request логин и пароль пользователя
     * @return accessToken, refreshToken и userName
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login attempt for user: {}", request.getLogin());
        AuthResponse response = authService.login(request);
        log.info("User logged in successfully: {}", request.getLogin());
        return ResponseEntity.ok(response);
    }

    /**
     * Обновление access токена с использованием refresh токена
     *
     * @param authorization заголовок с refresh токеном (Bearer {refresh_token})
     * @return новый accessToken и существующий refreshToken
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader("Authorization") String authorization) {
        log.info("Token refresh request received");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.warn("Invalid authorization header format");
            throw new RuntimeException("Invalid token format. Expected: Bearer {token}");
        }

        String refreshToken = authorization.substring(7);
        AuthResponse response = authService.refreshToken(refreshToken);
        log.info("Token refreshed successfully for user: {}", response.getUserName());
        return ResponseEntity.ok(response);
    }

    /**
     * Выход пользователя из системы - инвалидация токенов
     *
     * @param authorization заголовок с access токеном (Bearer {access_token})
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorization) {
        log.info("Logout request received");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            authService.logout(token);
            log.info("User logged out successfully");
        } else {
            log.warn("Logout attempt without valid authorization header");
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Проверка доступности имени пользователя (для регистрации)
     *
     * @param userName имя пользователя для проверки
     * @return true - имя свободно, false - уже занято
     */
    @GetMapping("/check/username")
    public ResponseEntity<Boolean> checkUsernameAvailability(
            @RequestParam @NotBlank(message = "Username cannot be empty")
            @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
            String userName) {
        log.info("Checking username availability: {}", userName);
        boolean isAvailable = authService.existsByUserName(userName);
        log.debug("Username '{}' available: {}", userName, isAvailable);
        return ResponseEntity.ok(isAvailable);
    }

    /**
     * Проверка доступности логина (для регистрации)
     *
     * @param login логин для проверки
     * @return true - логин свободен, false - уже занят
     */
    @GetMapping("/check/login")
    public ResponseEntity<Boolean> checkLoginAvailability(
            @RequestParam @NotBlank(message = "Login cannot be empty")
            @Size(min = 3, max = 30, message = "Login must be between 3 and 30 characters")
            String login) {
        log.info("Checking login availability: {}", login);
        boolean isAvailable = authService.existsByLogin(login);
        log.debug("Login '{}' available: {}", login, isAvailable);
        return ResponseEntity.ok(isAvailable);
    }

    /**
     * Регистрация нового пользователя
     *
     * @param dtoUser данные нового пользователя
     * @return UUID созданного пользователя
     */
    @PostMapping("/register")
    public ResponseEntity<UUID> registerUser(@Valid @RequestBody DtoUser dtoUser) {
        log.info("Registering new user: {}", dtoUser.getUserName());
        UUID userId = authService.saveUser(dtoUser);
        log.info("User registered successfully with ID: {}", userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(userId);
    }
}