package org.example.authservice.service;

import lombok.RequiredArgsConstructor;
import org.example.authservice.repository.UserRepository;
import org.example.authservice.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    @Transactional
    public void save(User user){
        userRepository.save(user);
    }

    @Transactional
    public void remove(User user){
        userRepository.delete(user);
    }

    @Transactional
    public void removeAll(){
        userRepository.deleteAll();
    }

    @Transactional
    public List<User> findAll(){
        return userRepository.findAll();
    }
}