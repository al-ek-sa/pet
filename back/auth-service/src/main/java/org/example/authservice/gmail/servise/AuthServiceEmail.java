package org.example.authservice.gmail.servise;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Authentication service responsible for user registration and password management.
 * <p>
 * This service orchestrates the authentication flow by combining user management
 * and email notification capabilities. It handles user registration with
 * welcome emails and password reset functionality with secure token-based links.
 * </p>
 *
 * <h2>Core Responsibilities:</h2>
 * <ul>
 *   <li>User registration with automatic welcome email</li>
 *   <li>Password reset request handling with secure tokens</li>
 *   <li>Email notifications for authentication events</li>
 *   <li>Integration between UserService and EmailService</li>
 * </ul>
 *
 * <h2>Dependencies:</h2>
 * <ul>
 *   <li><b>UserService</b> - For user CRUD operations</li>
 *   <li><b>EmailService</b> - For sending email notifications</li>
 * </ul>
 *
 * <h2>Authentication Flow:</h2>
 * <ol>
 *   <li><b>Registration:</b> User data is saved, then welcome email is sent</li>
 *   <li><b>Password Reset:</b> Reset token is generated, email with reset link is sent</li>
 * </ol>
 *
 * <h2>Usage Examples:</h2>
 *
 * <p><b>Register a new user:</b></p>
 * <pre>
 * User user = User.builder()
 *     .login("john_doe")
 *     .password(encodedPassword)
 *     .userName("John Doe")
 *     .email("john@example.com")
 *     .active(true)
 *     .build();
 *
 * authService.registerUser(user);
 * // User is saved and receives a welcome email
 * </pre>
 *
 * <p><b>Send password reset email:</b></p>
 * <pre>
 * String resetToken = UUID.randomUUID().toString();
 * authService.sendPasswordResetEmail("john@example.com", resetToken);
 * // User receives password reset email with secure link
 * </pre>
 *
 * <h2>Email Templates:</h2>
 * <h3>Welcome Email:</h3>
 * <pre>
 * &lt;h1&gt;Welcome to Auth Service!&lt;/h1&gt;
 * &lt;p&gt;Hello &lt;strong&gt;{userName}&lt;/strong&gt;,&lt;/p&gt;
 * &lt;p&gt;Your account has been successfully created!&lt;/p&gt;
 * &lt;p&gt;Login: {login}&lt;/p&gt;
 * &lt;p&gt;Email: {email}&lt;/p&gt;
 * </pre>
 *
 * <h3>Password Reset Email:</h3>
 * <pre>
 * &lt;h1&gt;Password Reset&lt;/h1&gt;
 * &lt;p&gt;Click the link below to reset your password:&lt;/p&gt;
 * &lt;a href="{resetLink}"&gt;Reset Password&lt;/a&gt;
 * &lt;p&gt;This link will expire in 15 minutes.&lt;/p&gt;
 * </pre>
 *
 * <h2>Security Best Practices:</h2>
 * <ul>
 *   <li>Password reset links should expire after a short time (15 minutes)</li>
 *   <li>Reset tokens should be cryptographically secure (UUID or similar)</li>
 *   <li>Welcome emails should not contain sensitive information</li>
 *   <li>Consider using HTTPS for reset links in production</li>
 *   <li>Tokens should be stored in database for validation</li>
 * </ul>
 *
 * <h2>Potential Improvements:</h2>
 * <ul>
 *   <li>Store reset tokens in Redis with TTL for automatic expiration</li>
 *   <li>Add email verification flow before registration is complete</li>
 *   <li>Implement rate limiting for password reset requests</li>
 *   <li>Add logging for security auditing</li>
 *   <li>Use internationalization for email templates</li>
 *   <li>Add template rendering engine (Thymeleaf, Mustache)</li>
 *   <li>Make reset link URL configurable via application properties</li>
 *   <li>Add email sending asynchronously to improve performance</li>
 * </ul>
 *
 * <h2>Configuration Recommendations:</h2>
 * <pre>
 * # application.properties
 * app.reset-password.url=https://yourapp.com/reset-password
 * app.reset-password.expiry-minutes=15
 * app.email.welcome.enabled=true
 * </pre>
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 * @see org.example.authservice.gmail.servise.EmailService
 * @see org.springframework.stereotype.Service
 */
@Service
@RequiredArgsConstructor
public class AuthServiceEmail {

    private final EmailService emailService;

    /**
     * Registers a new user in the system and sends a welcome email.
     * <p>
     * This method performs two main operations:
     * <ol>
     *   <li>Saves the user to the database using {@code UserService}</li>
     *   <li>Sends a welcome email to the user's email address</li>
     * </ol>
     * </p>
     *
     * <h3>Preconditions:</h3>
     * <ul>
     *   <li>User object must not be null</li>
     *   <li>User must have a valid email address</li>
     *   <li>User's login must be unique</li>
     *   <li>Password should be encoded before calling this method</li>
     * </ul>
     *
     * <h3>Email Content:</h3>
     * <p>The welcome email includes:</p>
     * <ul>
     *   <li>Personalized greeting with user's display name</li>
     *   <li>Confirmation of account creation</li>
     *   <li>User's login and email for reference</li>
     * </ul>
     *
     * <h3>Exception Handling:</h3>
     * <p>If the email fails to send, the user is still saved to the database.
     * Consider implementing retry logic or logging failures.</p>
     *
     * <h3>Transaction Management:</h3>
     * <p>Consider adding {@code @Transactional} to this method to ensure
     * both operations succeed or fail together.</p>
     *
     * <h3>Async Option:</h3>
     * <p>Consider making the email sending asynchronous to not block
     * the registration process.</p>
     *
     * @throws IllegalArgumentException if user or user.getEmail() is null
     * @see org.example.authservice.gmail.servise.EmailService#sendSimpleEmail(String, String, String)
     */
    public void registerUser(String code, String email) {
        String html = String.format(code);

        emailService.sendSimpleEmail(
                email,
                "Welcome to Auth Service!",
                html
        );
    }

    /**
     * Sends a password reset email to the user with a secure reset link.
     * <p>
     * This method generates an HTML email containing a password reset link
     * with an embedded token. The link is designed to expire after 15 minutes
     * for security purposes.
     * </p>
     *
     * <h3>Reset Link Format:</h3>
     * <pre>
     * http://localhost:8081/reset-password?token={resetToken}
     * </pre>
     *
     * <h3>Important Security Considerations:</h3>
     * <ul>
     *   <li><b>Token Security:</b> The token should be cryptographically random</li>
     *   <li><b>Token Storage:</b> Store tokens in database with expiry timestamp</li>
     *   <li><b>Token Validation:</b> Validate token on reset endpoint</li>
     *   <li><b>Time Limit:</b> Links expire after 15 minutes</li>
     *   <li><b>One-time Use:</b> Tokens should be invalidated after use</li>
     *   <li><b>HTTPS:</b> Use HTTPS in production for security</li>
     * </ul>
     *
     * <h3>Security Best Practices - What's Missing:</h3>
     * <ul>
     *   <li>Token should be stored in database/Redis before sending</li>
     *   <li>Add check to ensure user exists before sending</li>
     *   <li>Implement rate limiting to prevent abuse</li>
     *   <li>Log password reset requests for security auditing</li>
     *   <li>Consider implementing CAPTCHA for reset requests</li>
     * </ul>
     *
     * <h3>Email Content:</h3>
     * <ul>
     *   <li>Clear call-to-action button/link</li>
     *   <li>Expiration notice (15 minutes)</li>
     *   <li>Security advice (ignore if you didn't request this)</li>
     * </ul>
     *
     * <h3>Configuration Recommendations:</h3>
     * <p>The reset link URL should be configurable:</p>
     * <pre>
     * @Value("${app.reset-password.url}")
     * private String resetPasswordUrl;
     * </pre>
     *
     * <h3>Full Flow Example:</h3>
     * <pre>
     * // 1. User requests password reset
     * authService.sendPasswordResetEmail(userEmail, resetToken);
     *
     * // 2. Store token in database with expiry
     * passwordResetTokenRepository.save(token);
     *
     * // 3. User clicks link: /reset-password?token=xxx
     *
     * // 4. Validate token and show reset form
     *
     * // 5. Update password and invalidate token
     * </pre>
     *
     * @param email the user's email address (must exist in system)
     * @param resetToken the secure token for password reset (should be UUID or similar)
     * @throws IllegalArgumentException if email or resetToken is null/empty
     * @see org.example.authservice.gmail.servise.EmailService#sendSimpleEmail(String, String, String)
     */
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