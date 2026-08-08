package org.example.authservice.bd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.bd.config.SecurityConfigPassword;
import org.example.authservice.bd.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PasswordService {

    private final UserRepository repository;
    public final SecurityConfigPassword securityConfigPassword;

    public int updatePassword(String login, String password){
        return repository.updatePassword(login, securityConfigPassword.passwordEncoder().encode(password));
    }
}
