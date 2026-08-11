package org.example.authservice.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.bd.entity.User;
import org.example.authservice.bd.repository.UserRepository;
import org.example.authservice.dto.AuthResponse;
import org.example.authservice.dto.DtoUser;
import org.example.authservice.dto.LoginRequest;
import org.example.authservice.dto.LoginName;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceJwt {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for user: {}", request.getLogin());

        Optional<LoginName> user = repository.findByLoginNativePassword(request.getLogin());

        if (user.isEmpty()) {
            log.warn("User not found: {}", request.getLogin());
            throw new RuntimeException("Invalid credentials");
        }

        if (!encoder.matches(request.getPassword(), user.get().getPassword())) {
            log.warn("Invalid password for user: {}", request.getLogin());
            throw new RuntimeException("Invalid credentials");
        }

        String accessToken = jwtService.generateAccessToken(
                request.getLogin(),
                user.get().getUserName()
        );

        String refreshToken = jwtService.generateRefreshToken(
                request.getLogin(),
                user.get().getUserName()
        );

        jwtService.saveRefreshToken(request.getLogin(), refreshToken);

        log.info("User logged in successfully: {}", request.getLogin());

        return new AuthResponse(
                accessToken,
                refreshToken,
                user.get().getUserName()
        );
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String login = jwtService.extractLogin(refreshToken);

        if (!jwtService.validateRefreshToken(login, refreshToken)) {
            throw new RuntimeException("Refresh token not found or invalid");
        }

        String newAccessToken = jwtService.generateAccessToken(login, getUsernameByLogin(login));

        log.info("Token refreshed for user: {}", login);

        return new AuthResponse(
                newAccessToken,
                refreshToken,
                getUsernameByLogin(login)
        );
    }

    public void logout(String token) {
        String login = jwtService.extractLogin(token);
        jwtService.blacklistToken(token);
        jwtService.deleteRefreshToken(login);
        log.info("User logged out: {}", login);
    }

    private String getUsernameByLogin(String login) {
        return repository.findByLoginNativePassword(login)
                .map(LoginName::getUserName)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean existsByUserName(String userName) {
        return repository.existsByUserName(userName);
    }

    public boolean existsByLogin(String login) {
        return repository.existsByLogin(login);
    }

    public UUID saveUser(DtoUser dtoUser) {
        User user = new User();
        user.setLogin(dtoUser.getLogin());
        user.setEmail(dtoUser.getEmail());
        user.setUserName(dtoUser.getUserName());
        user.setPassword(encoder.encode(dtoUser.getPassword()));
        return repository.save(user).getId();
    }
}