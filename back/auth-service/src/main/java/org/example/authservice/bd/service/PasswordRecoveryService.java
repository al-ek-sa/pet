package org.example.authservice.bd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.bd.config.SecurityConfigPassword;
import org.example.authservice.bd.repository.PasswordRecoveryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PasswordRecoveryService {

    public final PasswordRecoveryRepository passwordRecoveryRepository;
    public final SecurityConfigPassword securityConfigPassword;

    public int updatePassword(String login, String password){
        return passwordRecoveryRepository.updatePassword(login, securityConfigPassword.passwordEncoder().encode(password));
    }
}
