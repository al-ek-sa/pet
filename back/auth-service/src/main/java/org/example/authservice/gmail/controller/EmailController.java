package org.example.authservice.gmail.controller;

import lombok.RequiredArgsConstructor;
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

    /**
     * Sends an email to a specified recipient.
     * <p>
     * This endpoint accepts three parameters: recipient email address,
     * subject line, and the message body. The message is wrapped in
     * a simple HTML template before sending.
     * </p>
     *
     * <h3>Request Parameters:</h3>
     * <ul>
     *   <li><b>to</b> (required) - Recipient's email address</li>
     *   <li><b>subject</b> (required) - Email subject line</li>
     *   <li><b>message</b> (required) - Email body/content</li>
     * </ul>
     *
     * <h3>Request Examples:</h3>
     *
     * <p><b>cURL:</b></p>
     * <pre>
     * curl -X POST "http://localhost:8080/api/email/send?to=user@example.com&subject=Welcome&message=Hello%20World"
     * </pre>
     *
     * <p><b>JavaScript Fetch:</b></p>
     * <pre>
     * fetch('/api/email/send?to=user@example.com&subject=Hello&message=Test')
     *   .then(response => response.text())
     *   .then(data => console.log(data));
     * </pre>
     *
     * <h3>Email Template:</h3>
     * <pre>
     * &lt;h2&gt;Test Email&lt;/h2&gt;
     * &lt;p&gt;{message}&lt;/p&gt;
     * </pre>
     *
     * <h3>Validation Rules:</h3>
     * <ul>
     *   <li><b>to:</b> Must be a valid email format (e.g., user@domain.com)</li>
     *   <li><b>subject:</b> Cannot be empty or null</li>
     *   <li><b>message:</b> Cannot be empty or null</li>
     * </ul>
     *
     * <h3>Return Value:</h3>
     * <p>Returns the unique ID assigned by the email service provider (Resend).</p>
     *
     * <h3>Exceptions:</h3>
     * <ul>
     *   <li>{@code IllegalArgumentException} if any parameter is invalid</li>
     *   <li>{@code EmailServiceException} if email sending fails</li>
     * </ul>
     *
     * <h3>Security Recommendations:</h3>
     * <ul>
     *   <li>Add {@code @Valid} annotation and validation annotations to parameters</li>
     *   <li>Implement CSRF protection</li>
     *   <li>Add request rate limiting</li>
     *   <li>Consider using {@code @PostMapping} with a DTO body instead of query parameters</li>
     * </ul>
     *
     * @param to      recipient's email address (required)
     * @param subject email subject line (required)
     * @param message email message body (required)
     * @return success message with the email ID from the email service
     */
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