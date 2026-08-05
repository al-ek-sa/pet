package org.example.authservice.gmail.servise;

import lombok.RequiredArgsConstructor;
import org.example.authservice.entity.User;
import org.example.authservice.bd.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmailService emailService;
    private final UserService userService;

    public void registerUser(User user) {
        userService.save(user);
        String html = String.format("""
                <h1>Welcome to Auth Service!</h1>
                <p>Hello <strong>%s</strong>,</p>
                <p>Your account has been successfully created!</p>
                <p>Login: %s</p>
                <p>Email: %s</p>
                """,
                user.getUserName(),
                user.getLogin(),
                user.getEmail()
        );

        emailService.sendSimpleEmail(
                user.getEmail(),
                "Welcome to Auth Service!",
                html
        );
    }

    public void sendPasswordResetEmail(String email, String resetToken) {
        String resetLink = "http://localhost:8081/reset-password?token=" + resetToken;

        String html = String.format("""
                <h1>Password Reset</h1>
                <p>Click the link below to reset your password:</p>
                <a href="%s">Reset Password</a>
                <p>This link will expire in 15 minutes.</p>
                """,
                resetLink
        );

        emailService.sendSimpleEmail(
                email,
                "Password Reset Request",
                html
        );
    }
}
