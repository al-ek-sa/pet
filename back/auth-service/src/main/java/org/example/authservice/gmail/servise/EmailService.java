package org.example.authservice.gmail.servise;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.bd.repository.UserRepository;
import org.example.authservice.dto.EmailDtoTwo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for sending emails using the Resend email service provider.
 * <p>
 * This service provides functionality to send both HTML and plain text emails
 * through the Resend API. It handles the creation of email requests, sending
 * them via the Resend client, and logging the results.
 * </p>
 *
 * <h2>Core Features:</h2>
 * <ul>
 *   <li>Send HTML-formatted emails</li>
 *   <li>Send plain text emails</li>
 *   <li>Automatic error handling and logging</li>
 *   <li>Configurable sender email address</li>
 *   <li>Returns email ID for tracking</li>
 * </ul>
 *
 * <h2>Configuration Properties:</h2>
 * <p>The following properties must be defined in {@code application.properties}:</p>
 * <pre>
 * resend.api.key=${RESEND_API_KEY}
 * resend.from.email=noreply@yourapp.com
 * </pre>
 *
 * <h2>Dependencies:</h2>
 * <ul>
 *   <li><b>Resend client</b> - For sending emails via Resend API</li>
 *   <li><b>Slf4j</b> - For logging operations and errors</li>
 * </ul>
 *
 * <h2>Usage Examples:</h2>
 *
 * <p><b>Send HTML email:</b></p>
 * <pre>
 * String html = "&lt;h1&gt;Welcome!&lt;/h1&gt;&lt;p&gt;Hello world&lt;/p&gt;";
 * String emailId = emailService.sendSimpleEmail(
 *     "user@example.com",
 *     "Welcome Email",
 *     html
 * );
 * System.out.println("Email sent with ID: " + emailId);
 * </pre>
 *
 * <p><b>Send plain text email:</b></p>
 * <pre>
 * emailService.sendTextEmail(
 *     "user@example.com",
 *     "Welcome Email",
 *     "Hello world, this is a plain text email."
 * );
 * </pre>
 *
 * <h2>Email Types:</h2>
 * <ul>
 *   <li><b>HTML Email:</b> Supports rich formatting, images, links, and styling</li>
 *   <li><b>Text Email:</b> Plain text format, suitable for simple notifications</li>
 * </ul>
 *
 * <h2>Error Handling:</h2>
 * <ul>
 *   <li>All Resend exceptions are caught and wrapped in RuntimeException</li>
 *   <li>Errors are logged using SLF4J for debugging</li>
 *   <li>Application-level error handling should catch RuntimeException</li>
 * </ul>
 *
 * <h2>Security Best Practices:</h2>
 * <ul>
 *   <li>Never expose API keys in code or logs</li>
 *   <li>Use environment variables for sensitive configuration</li>
 *   <li>Validate email addresses before sending</li>
 *   <li>Sanitize email content to prevent injection attacks</li>
 *   <li>Implement rate limiting to prevent spam</li>
 *   <li>Use different sender emails for different environments</li>
 * </ul>
 *
 * <h2>Potential Improvements:</h2>
 * <ul>
 *   <li>Add email validation (format, domain existence)</li>
 *   <li>Implement retry mechanism for failed emails</li>
 *   <li>Add email queue for reliability</li>
 *   <li>Support attachments</li>
 *   <li>Add email tracking (opens, clicks)</li>
 *   <li>Use {@code @Async} for non-blocking email sending</li>
 *   <li>Create custom exceptions for different error scenarios</li>
 *   <li>Add support for CC and BCC recipients</li>
 *   <li>Implement email templates with Thymeleaf</li>
 * </ul>
 *
 * <h2>Exception Scenarios:</h2>
 * <ul>
 *   <li><b>Invalid API Key:</b> ResendException with authentication error</li>
 *   <li><b>Invalid Email Format:</b> ResendException with invalid email error</li>
 *   <li><b>Rate Limiting:</b> ResendException with rate limit exceeded</li>
 *   <li><b>Network Issues:</b> ResendException with connection timeout</li>
 * </ul>
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 * @see com.resend.Resend
 * @see com.resend.services.emails.model.SendEmailRequest
 * @see com.resend.services.emails.model.SendEmailResponse
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final Resend resend;


    @Value("${resend.from.email}")
    private String fromEmail;

    private final UserRepository repository;

    public String sendSimpleEmail(String to, String subject, String htmlContent) {
        try {
            SendEmailRequest request = SendEmailRequest.builder()
                    .from(fromEmail)
                    .to(to)
                    .subject(subject)
                    .html(htmlContent)
                    .build();

            SendEmailResponse response = resend.emails().send(request);
            log.info("Email sent successfully to {} with ID: {}", to, response.getId());
            return response.getId();

        } catch (ResendException e) {
            log.error("Failed to send HTML email to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send email to " + to, e);
        }
    }

    public String sendEmail(EmailDtoTwo dto, String html){
        if(repository.existsByLoginAndEmail(dto.getLogin(), dto.getEmail())) return sendSimpleEmail(dto.getEmail(),dto.getCode(), html);
        return "fail";
    }
}