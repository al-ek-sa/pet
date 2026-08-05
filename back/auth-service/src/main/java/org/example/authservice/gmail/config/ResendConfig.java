package org.example.authservice.gmail.config;

import com.resend.Resend;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up the Resend email service client.
 * <p>
 * This configuration creates and manages a singleton instance of the Resend client
 * that is used for sending emails throughout the application. The API key is
 * injected from the application properties file.
 * </p>
 *
 * <h2>Purpose:</h2>
 * <ul>
 *   <li>Provides a centralized configuration for email service</li>
 *   <li>Creates a singleton Resend client bean</li>
 *   <li>Securely loads API key from external configuration</li>
 *   <li>Enables dependency injection of Resend client throughout the application</li>
 * </ul>
 *
 * <h2>Configuration Properties:</h2>
 * <p>The following property must be defined in {@code application.properties}
 * or {@code application.yml}:</p>
 * <pre>
 * resend.api.key=${RESEND_API_KEY}
 * </pre>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 * &#64;Service
 * public class EmailService {
 *     private final Resend resend;
 *
 *     public EmailService(Resend resend) {
 *         this.resend = resend;
 *     }
 *
 *     public void sendEmail(String to, String subject, String body) {
 *         // Use resend client to send email
 *     }
 * }
 * </pre>
 *
 * <h2>Security Best Practices:</h2>
 * <ul>
 *   <li>Never hardcode API keys in source code</li>
 *   <li>Use environment variables for production:</li>
 *   <li>Store API key securely using {@code RESEND_API_KEY} environment variable</li>
 *   <li>Keep API key out of version control</li>
 * </ul>
 *
 * <h2>Environment Setup:</h2>
 * <p>For production, set the environment variable:</p>
 * <pre>
 * export RESEND_API_KEY=re_your_api_key_here
 * </pre>
 *
 * <p>For development, add to {@code application.properties}:</p>
 * <pre>
 * resend.api.key=re_test_your_test_key
 * </pre>
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 * @see com.resend.Resend
 * @see org.springframework.beans.factory.annotation.Value
 * @see org.springframework.context.annotation.Configuration
 */
@Configuration
public class ResendConfig {

    /**
     * The API key for authenticating with the Resend email service.
     * <p>
     * This key is injected from the application properties using the
     * placeholder {@code ${resend.api.key}}.
     * </p>
     *
     * <h3>Security Notes:</h3>
     * <ul>
     *   <li>This value should NEVER be hardcoded or committed to version control</li>
     *   <li>Use environment variables for production deployments</li>
     *   <li>Different keys should be used for development and production</li>
     *   <li>Consider using Spring Cloud Config or Vault for enhanced security</li>
     * </ul>
     *
     * <h3>Key Types:</h3>
     * <ul>
     *   <li><b>Test Keys:</b> Start with {@code re_test_}, can be used in development</li>
     *   <li><b>Live Keys:</b> Start with {@code re_}, should be used in production</li>
     * </ul>
     *
     * @see org.springframework.core.env.Environment
     */
    @Value("${resend.api.key}")
    private String apiKey;

    /**
     * Creates and configures a singleton Resend client bean.
     * <p>
     * This bean is instantiated once and reused throughout the application
     * lifecycle. The Resend client is thread-safe and can be shared across
     * multiple components.
     * </p>
     *
     * <h3>Bean Scope:</h3>
     * <p>Singleton (default) - one instance per Spring container</p>
     *
     * <h3>Initialization:</h3>
     * <ul>
     *   <li>Bean is created when the application context loads</li>
     *   <li>The API key is injected before bean creation</li>
     *   <li>Resend client is initialized with the provided API key</li>
     * </ul>
     *
     * <h3>Error Handling:</h3>
     * <ul>
     *   <li>If the API key is missing or invalid, bean creation will fail</li>
     *   <li>Application startup will be aborted with a clear error message</li>
     *   <li>Consider adding a fallback mechanism for graceful degradation</li>
     * </ul>
     *
     * <h3>Dependencies:</h3>
     * <ul>
     *   <li>Requires {@code com.resend:resend-java} dependency in classpath</li>
     *   <li>Requires {@code resend.api.key} property to be defined</li>
     * </ul>
     *
     * @return configured Resend client instance
     * @throws org.springframework.beans.factory.BeanCreationException if API key is missing
     * @see com.resend.Resend
     */
    @Bean
    public Resend resend() {
        return new Resend(apiKey);
    }
}