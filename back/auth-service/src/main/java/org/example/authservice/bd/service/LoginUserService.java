package org.example.authservice.bd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.bd.config.SecurityConfigPassword;
import org.example.authservice.bd.repository.LoginUserRepository;
import org.example.authservice.dto.LoginName;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LoginUserService {

    private final LoginUserRepository loginUserRepository;
    private final SecurityConfigPassword securityConfigPassword;

    public String findByLoginAndPassword(String login, String password) {
        Optional<LoginName> user = loginUserRepository.findByLoginNativePassword(login);
        if(securityConfigPassword.passwordEncoder().matches(password, user.get().getPassword())){
            return user.get().getUserName();
        }
        return "неверные данные";
    }
}
