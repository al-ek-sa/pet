package org.example.authservice.gmail.controller;

import lombok.RequiredArgsConstructor;
import org.example.authservice.gmail.servise.EmailService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestParam String to,
                            @RequestParam String subject,
                            @RequestParam String message) {

        String html = String.format("""
                <h2>Test Email</h2>
                <p>%s</p>
                """, message);

        String emailId = emailService.sendSimpleEmail(to, subject, html);
        return "Email sent! ID: " + emailId;
    }
}