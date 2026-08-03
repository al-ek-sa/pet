package org.example.authservice;

import lombok.RequiredArgsConstructor;
import org.example.authservice.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User register(String login, String password, String userName, String email) {
        if (userRepository.findByLoginNative(login).isPresent()) {
            throw new RuntimeException("Login already exists");
        }

        User user = User.builder()
                .login(login)
                .password(password)
                .userName(userName)
                .email(email)
                .active(true)
                .build();

        return userRepository.save(user);
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User findByLogin(String login) {
        return userRepository.findByLoginNative(login)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User login(String login, String password) {
        User user = userRepository.findByLoginNative(login)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> searchByUserName(String keyword) {
        return userRepository.findByUserNameNative(keyword);
    }

    @Transactional
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    public boolean existsByLogin(String login) {
        return userRepository.findByLoginNative(login).isPresent();
    }
}