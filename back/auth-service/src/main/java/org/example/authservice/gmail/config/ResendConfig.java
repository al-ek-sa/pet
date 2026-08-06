package org.example.authservice.gmail.config;

import com.resend.Resend;
import lombok.extern.slf4j.Slf4j;
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
 *   <li>Comprehensive logging for configuration and initialization</li>
 * </ul>
 *
 * <h2>Logging:</h2>
 * <ul>
 *   <li><b>INFO:</b> Configuration loading and bean initialization</li>
 *   <li><b>DEBUG:</b> Detailed configuration steps (API key masking)</li>
 *   <li><b>WARN:</b> Potential configuration issues</li>
 *   <li><b>ERROR:</b> Configuration failures</li>
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
 *   <li>Use environment variables for production</li>
 *   <li>Store API key securely using {@code RESEND_API_KEY} environment variable</li>
 *   <li>Keep API key out of version control</li>
 *   <li>API key is never logged in plain text (masked for security)</li>
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
 * <h2>Logging Configuration:</h2>
 * <p>To enable detailed logging, add to {@code application.properties}:</p>
 * <pre>
 * logging.level.org.example.authservice.gmail.config.ResendConfig=DEBUG
 * </pre>
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 * @see com.resend.Resend
 * @see org.springframework.beans.factory.annotation.Value
 * @see org.springframework.context.annotation.Configuration
 */
@Configuration
@Slf4j
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
     *   <li>API key is masked in logs for security</li>
     * </ul>
     *
     * <h3>Key Types:</h3>
     * <ul>
     *   <li><b>Test Keys:</b> Start with {@code re_test_}, can be used in development</li>
     *   <li><b>Live Keys:</b> Start with {@code re_}, should be used in production</li>
     * </ul>
     *
     * <h3>Logging:</h3>
     * <ul>
     *   <li><b>INFO:</b> API key configuration loaded</li>
     *   <li><b>DEBUG:</b> API key presence verification (masked)</li>
     *   <li><b>WARN:</b> If API key appears to be a test key in production</li>
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
     * <h3>Logging:</h3>
     * <ul>
     *   <li><b>INFO:</b> Bean initialization start and completion</li>
     *   <li><b>DEBUG:</b> API key validation and configuration details</li>
     *   <li><b>WARN:</b> Potential configuration issues</li>
     *   <li><b>ERROR:</b> Bean creation failures</li>
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
        log.info("Initializing Resend email client configuration");
        log.debug("Resend configuration started - environment: {}",
                System.getProperty("spring.profiles.active", "default"));

        String maskedKey = maskApiKey(apiKey);
        log.debug("API key loaded: {}", maskedKey);
        log.info("Resend API key configuration loaded successfully");

        if (apiKey == null || apiKey.trim().isEmpty()) {
            log.error("Resend API key is null or empty! Please configure 'resend.api.key' property");
            throw new IllegalStateException("Resend API key is required but not configured");
        }

        if (apiKey.startsWith("re_test_")) {
            log.warn("Using Resend TEST API key. This should not be used in production environments");
        }

        try {
            log.debug("Creating Resend client instance with API key: {}", maskedKey);
            Resend resend = new Resend(apiKey);
            log.info("Resend email client bean created successfully");
            log.debug("Resend client instance initialized - {}", resend.getClass().getName());
            return resend;
        } catch (Exception e) {
            log.error("Failed to create Resend client bean. Error: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to initialize Resend email client", e);
        }
    }

    /**
     * Masks the API key for secure logging.
     * <p>
     * Only shows first 4 and last 4 characters of the API key to prevent
     * accidental exposure in logs.
     * </p>
     *
     * <h3>Examples:</h3>
     * <ul>
     *   <li>"re_test_1234567890" → "re_t...7890"</li>
     *   <li>"re_1234567890abcdef" → "re_1...cdef"</li>
     *   <li>null or empty → "null" or "empty"</li>
     * </ul>
     *
     * @param key the API key to mask
     * @return masked API key string
     */
    private String maskApiKey(String key) {
        if (key == null) {
            return "null";
        }
        if (key.trim().isEmpty()) {
            return "empty";
        }
        if (key.length() <= 8) {
            return "***";
        }
        return key.substring(0, 4) + "..." + key.substring(key.length() - 4);
    }
}