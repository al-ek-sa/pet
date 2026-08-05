package org.example.authservice.gmail.servise;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final Resend resend;

    @Value("${resend.from.email}")
    private String fromEmail;
    public String sendSimpleEmail(String to, String subject, String htmlContent) {
        try {
            SendEmailRequest request = SendEmailRequest.builder()
                    .from(fromEmail)
                    .to(to)
                    .subject(subject)
                    .html(htmlContent)
                    .build();

            SendEmailResponse response = resend.emails().send(request);
            return response.getId();

        } catch (ResendException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendTextEmail(String to, String subject, String text) {
        try {
            SendEmailRequest request = SendEmailRequest.builder()
                    .from(fromEmail)
                    .to(to)
                    .subject(subject)
                    .text(text)
                    .build();

            resend.emails().send(request);

        } catch (ResendException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}