package org.example.authservice.bd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.bd.config.SecurityConfigPassword;
import org.example.authservice.bd.entity.User;
import org.example.authservice.bd.repository.UserRepository;
import org.example.authservice.dto.LoginName;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final UserRepository repository;
    private final SecurityConfigPassword securityConfig;

    /**это для входа(сделать без передачи пароля с бд)**/
    public String findByLoginAndPassword(String login, String password) {
        Optional<LoginName> user = repository.findByLoginNativePassword(login);
        if (!securityConfig.passwordEncoder().matches(password, user.get().getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return user.get().getUserName();
    }

    /**проверка или данное имя уже занято(для регистрации)**/
    public boolean existsByUserName(String userName){
        return repository.existsByUserName(userName);
    }

    /**проверка или данный логин занят (для регистрации)**/
    public boolean existsByLogin(String login){
        return repository.existsByLogin(login);
    }

    /**создание нового пользователя при регистрации**/
    public UUID saveUser(User user){
        user.setPassword(securityConfig.passwordEncoder().encode(user.getPassword()));
        return repository.save(user).getId();
    }
}
