package org.example.authservice.gmail.controller;

import lombok.RequiredArgsConstructor;
import org.example.authservice.dto.EmailDtoOne;
import org.example.authservice.dto.EmailDtoTwo;
import org.example.authservice.gmail.servise.EmailService;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling email-related operations.
 * <p>
 * This controller provides endpoints for sending emails through the
 * Resend email service. All endpoints are prefixed with {@code /api/email}.
 * </p>
 *
 * <p><b>Base URL:</b> {@code /api/email}</p>
 *
 * <h2>Available Endpoints:</h2>
 * <ul>
 *   <li><b>POST /api/email/send</b> - Send an email to a recipient</li>
 * </ul>
 *
 * <h2>Features:</h2>
 * <ul>
 *   <li>Simple email sending functionality</li>
 *   <li>HTML email support with customizable content</li>
 *   <li>Returns email ID from the email service provider</li>
 *   <li>Uses dependency injection via {@code @RequiredArgsConstructor}</li>
 * </ul>
 *
 * <h2>Email Format:</h2>
 * <p>The email is sent in HTML format with the following structure:</p>
 * <pre>
 * &lt;h2&gt;Test Email&lt;/h2&gt;
 * &lt;p&gt;{user_message}&lt;/p&gt;
 * </pre>
 *
 * <h2>Usage Examples:</h2>
 *
 * <p><b>Send a simple email:</b></p>
 * <pre>
 * POST /api/email/send?to=john@example.com&subject=Hello&message=This is a test
 * </pre>
 *
 * <p><b>Response:</b></p>
 * <pre>
 * "Email sent! ID: 123e4567-e89b-12d3-a456-426614174000"
 * </pre>
 *
 * <h2>Response Codes:</h2>
 * <ul>
 *   <li><b>200 OK</b> - Email sent successfully</li>
 *   <li><b>400 Bad Request</b> - Invalid parameters (missing or empty values)</li>
 *   <li><b>500 Internal Server Error</b> - Email service failure</li>
 * </ul>
 *
 * <h2>Security Considerations:</h2>
 * <ul>
 *   <li>Consider adding authentication to prevent abuse</li>
 *   <li>Add rate limiting to prevent spam</li>
 *   <li>Validate email addresses format</li>
 *   <li>Sanitize user input to prevent XSS attacks</li>
 *   <li>Consider using DTOs instead of request parameters</li>
 * </ul>
 *
 * <h2>Potential Improvements:</h2>
 * <ul>
 *   <li>Add email template support (Thymeleaf or Velocity)</li>
 *   <li>Implement asynchronous email sending</li>
 *   <li>Add email queue for better reliability</li>
 *   <li>Support attachments</li>
 *   <li>Add email tracking and analytics</li>
 *   <li>Implement email validation</li>
 * </ul>
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 * @see org.example.authservice.gmail.servise.EmailService
 * @see org.springframework.web.bind.annotation.RestController
 */
@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestBody EmailDtoOne request) {
        String html = """
                <h2>Test Email</h2>
                <p>%s</p>
                """;
        return emailService.sendSimpleEmail(request.getEmail(), request.getCode(), html);
    }

    @PostMapping("/login")
    public String send(@RequestBody EmailDtoTwo request) {
        String html = """
                <h2>Test Email</h2>
                <p>%s</p>
                """;
        return emailService.sendEmail(request, html);
    }
}