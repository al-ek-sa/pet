package org.example.authservice.gmail.servise;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * The sender email address for all outgoing emails.
     * <p>
     * This value is injected from application properties using the placeholder
     * {@code ${resend.from.email}}. The email must be verified in Resend.
     * </p>
     *
     * <h3>Configuration Example:</h3>
     * <pre>
     * resend.from.email=noreply@yourdomain.com
     * </pre>
     *
     * <h3>Important Notes:</h3>
     * <ul>
     *   <li>The email must be verified in your Resend account</li>
     *   <li>Use different sender emails for different environments</li>
     *   <li>Consider using subdomain for email sending (e.g., mail.yourdomain.com)</li>
     * </ul>
     *
     * @see org.springframework.beans.factory.annotation.Value
     */
    @Value("${resend.from.email}")
    private String fromEmail;

    /**
     * Sends an HTML-formatted email to a single recipient.
     * <p>
     * This method creates a rich HTML email with support for formatting,
     * images, links, and custom styling. The HTML content is passed as
     * a string and embedded directly into the email body.
     * </p>
     *
     * <h3>Request Parameters:</h3>
     * <ul>
     *   <li><b>to</b> - Recipient email address (can be a single address)</li>
     *   <li><b>subject</b> - Email subject line</li>
     *   <li><b>htmlContent</b> - HTML content for the email body</li>
     * </ul>
     *
     * <h3>HTML Content Guidelines:</h3>
     * <ul>
     *   <li>Use valid HTML markup</li>
     *   <li>Include inline CSS for better email client compatibility</li>
     *   <li>Use responsive design for mobile devices</li>
     *   <li>Test email templates across different email clients</li>
     *   <li>Avoid external resources that may be blocked</li>
     * </ul>
     *
     * <h3>Return Value:</h3>
     * <p>Returns the unique email ID assigned by Resend. This ID can be used
     * for tracking and debugging purposes.</p>
     *
     * <h3>Example HTML Content:</h3>
     * <pre>
     * String html = """
     *     &lt;h1 style="color: #333;"&gt;Welcome!&lt;/h1&gt;
     *     &lt;p&gt;Thank you for registering.&lt;/p&gt;
     *     &lt;a href="<a href="https://yourapp.com/verify">...</a>"&gt;Verify Email&lt;/a&gt;
     *     """;
     * </pre>
     *
     * <h3>Error Handling:</h3>
     * <ul>
     *   <li>Wraps {@code ResendException} in {@code RuntimeException}</li>
     *   <li>Logs detailed error message using SLF4J</li>
     *   <li>Throws runtime exception for application-level handling</li>
     * </ul>
     *
     * @param to recipient's email address
     * @param subject email subject line
     * @param htmlContent HTML content for the email body
     * @return unique email ID from Resend
     * @throws RuntimeException if email sending fails (wraps ResendException)
     * @see SendEmailRequest
     * @see SendEmailResponse
     */
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

    /**
     * Sends a plain text email to a single recipient.
     * <p>
     * This method sends an email in plain text format without any HTML
     * formatting. It is suitable for simple notifications, alerts, and
     * system messages.
     * </p>
     *
     * <h3>When to Use:</h3>
     * <ul>
     *   <li>System notifications and alerts</li>
     *   <li>Simple informational messages</li>
     *   <li>Logs and reports</li>
     *   <li>Emails to legacy email clients</li>
     *   <li>Automated system emails</li>
     * </ul>
     *
     * <h3>When to Use HTML Instead:</h3>
     * <ul>
     *   <li>Marketing and promotional emails</li>
     *   <li>Emails requiring formatting and styling</li>
     *   <li>Emails with images or links</li>
     *   <li>User-facing communications</li>
     * </ul>
     *
     * <h3>Request Parameters:</h3>
     * <ul>
     *   <li><b>to</b> - Recipient email address</li>
     *   <li><b>subject</b> - Email subject line</li>
     *   <li><b>text</b> - Plain text content for the email body</li>
     * </ul>
     *
     * <h3>Text Content Guidelines:</h3>
     * <ul>
     *   <li>Keep lines short (max 78 characters for compatibility)</li>
     *   <li>Use line breaks for readability</li>
     *   <li>Avoid special characters that may not render correctly</li>
     *   <li>Include contact information for replies</li>
     * </ul>
     *
     * <h3>Error Handling:</h3>
     * <ul>
     *   <li>Wraps {@code ResendException} in {@code RuntimeException}</li>
     *   <li>Logs detailed error message using SLF4J</li>
     *   <li>Throws runtime exception for application-level handling</li>
     * </ul>
     *
     * <h3>Difference from HTML Email:</h3>
     * <p>This method uses {@code .text()} instead of {@code .html()} in
     * the request builder, resulting in a plain text email without any
     * formatting or styling.</p>
     *
     * @param to recipient's email address
     * @param subject email subject line
     * @param text plain text content for the email body
     * @throws RuntimeException if email sending fails (wraps ResendException)
     * @see com.resend.services.emails.model.SendEmailRequest
     */
    public void sendTextEmail(String to, String subject, String text) {
        try {
            SendEmailRequest request = SendEmailRequest.builder()
                    .from(fromEmail)
                    .to(to)
                    .subject(subject)
                    .text(text)
                    .build();

            resend.emails().send(request);
            log.info("Text email sent successfully to {}", to);

        } catch (ResendException e) {
            log.error("Failed to send text email to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send email to " + to, e);
        }
    }
}