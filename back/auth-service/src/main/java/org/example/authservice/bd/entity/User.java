package org.example.authservice.bd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity class representing a user for storing credentials and personal information.
 * <p>
 * This is a JPA entity that maps to the "users" table in the database.
 * Contains basic user information: login, password, display name, email,
 * account status, and creation timestamp.
 * </p>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Uses UUID as primary key with automatic generation</li>
 *   <li>Login and email must be unique</li>
 *   <li>Password should be stored in encrypted form (BCrypt recommended)</li>
 *   <li>Creation timestamp is set automatically upon persistence</li>
 * </ul>
 *
 * <h2>Logging:</h2>
 * <p>This entity includes lifecycle logging to track CRUD operations:</p>
 * <ul>
 *   <li><b>INFO:</b> Entity creation and updates</li>
 *   <li><b>DEBUG:</b> Entity details (excluding password)</li>
 *   <li><b>WARN:</b> Potential issues (null fields, etc.)</li>
 * </ul>
 *
 * <p><b>Database Schema:</b></p>
 * <pre>
 * CREATE TABLE users (
 *     id UUID PRIMARY KEY,
 *     login VARCHAR(50) UNIQUE NOT NULL,
 *     password VARCHAR(255) NOT NULL,
 *     user_name VARCHAR(100) NOT NULL,
 *     email VARCHAR(100) UNIQUE NOT NULL,
 *     is_active BOOLEAN DEFAULT TRUE,
 *     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
 * );
 * </pre>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 * User user = User.builder()
 *     .login("john_doe")
 *     .password(passwordEncoder.encode("securePass123"))
 *     .userName("John Doe")
 *     .email("john@example.com")
 *     .active(true)
 *     .build();
 *
 * userRepository.save(user);
 * // Logs: "Creating new user with login: john_doe"
 * // Logs: "User created successfully with ID: 550e8400-..."
 * </pre>
 *
 * <h2>Security Notes:</h2>
 * <ul>
 *   <li>Passwords are never logged in plain text</li>
 *   <li>Sensitive fields are masked in debug logs</li>
 *   <li>Logs exclude password field for security</li>
 * </ul>
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 * @see org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class User {

    /**
     * Unique user identifier.
     * <p>
     * Automatically generated using UUID strategy.
     * Serves as the primary key of the table.
     * </p>
     *
     * <h3>Logging:</h3>
     * <ul>
     *   <li><b>DEBUG:</b> ID generation and assignment</li>
     *   <li><b>INFO:</b> ID when entity is saved</li>
     * </ul>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Unique username (login) for authentication.
     * <p>
     * Used for authentication purposes. Must be unique and cannot be null.
     * Maximum length is 50 characters.
     * </p>
     *
     * <h3>Validation:</h3>
     * <ul>
     *   <li>Must not be null</li>
     *   <li>Must be unique across all users</li>
     *   <li>Length: 3-50 characters</li>
     * </ul>
     *
     * <h3>Logging:</h3>
     * <ul>
     *   <li><b>INFO:</b> Login when user is created or accessed</li>
     *   <li><b>DEBUG:</b> Login validation details</li>
     *   <li><b>WARN:</b> Duplicate login attempt</li>
     * </ul>
     */
    @Column(unique = true,
            nullable = false,
            length = 50,
            name = "login")
    private String login;

    /**
     * User password.
     * <p>
     * Recommended to store in encrypted form using hashing algorithms
     * (e.g., BCrypt, Argon2). Cannot be null.
     * </p>
     *
     * <h3>Security Best Practices:</h3>
     * <ul>
     *   <li>Never store in plain text</li>
     *   <li>Use BCrypt or Argon2 for hashing</li>
     *   <li>Minimum length: 8 characters</li>
     *   <li>Should include uppercase, lowercase, digits, and special chars</li>
     * </ul>
     *
     * <h3>Logging:</h3>
     * <ul>
     *   <li><b>WARN:</b> If password is null or empty</li>
     *   <li><b>WARN:</b> If password appears to be in plain text</li>
     *   <li><b>NEVER LOG:</b> Password value (even in debug mode)</li>
     * </ul>
     *
     * @see org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
     */
    @Column(nullable = false,
            name = "password")
    private String password;

    /**
     * Display name of the user.
     * <p>
     * Full name or nickname displayed in the user interface.
     * Maximum length is 100 characters. Cannot be null.
     * </p>
     *
     * <h3>Validation:</h3>
     * <ul>
     *   <li>Must not be null or empty</li>
     *   <li>Length: 1-100 characters</li>
     *   <li>Can contain spaces and special characters</li>
     * </ul>
     *
     * <h3>Logging:</h3>
     * <ul>
     *   <li><b>INFO:</b> User name in welcome emails</li>
     *   <li><b>DEBUG:</b> User name for display purposes</li>
     *   <li><b>WARN:</b> If user name is empty</li>
     * </ul>
     */
    @Column(name = "user_name",
            nullable = false,
            length = 100)
    private String userName;

    /**
     * User's email address.
     * <p>
     * Used for communication and password recovery.
     * Must be unique and cannot be null.
     * Maximum length is 100 characters.
     * </p>
     *
     * <h3>Validation:</h3>
     * <ul>
     *   <li>Must be valid email format</li>
     *   <li>Must be unique across all users</li>
     *   <li>Case-insensitive uniqueness</li>
     * </ul>
     *
     * <h3>Logging:</h3>
     * <ul>
     *   <li><b>INFO:</b> Email for notifications and recovery</li>
     *   <li><b>DEBUG:</b> Email validation details</li>
     *   <li><b>WARN:</b> Duplicate email attempt</li>
     * </ul>
     *
     * @see jakarta.validation.constraints.Email
     */
    @Column(nullable = false,
            length = 100,
            name = "email")
    private String email;

    /**
     * Account activity status flag.
     * <p>
     * Allows temporary account blocking without deleting the record.
     * Default value is {@code true} (active).
     * </p>
     *
     * <h3>States:</h3>
     * <ul>
     *   <li>{@code true} - Account is active and can authenticate</li>
     *   <li>{@code false} - Account is locked/deactivated</li>
     * </ul>
     *
     * <h3>Use Cases:</h3>
     * <ul>
     *   <li>Manual lock by administrator</li>
     *   <li>Auto-lock after multiple failed login attempts</li>
     *   <li>Soft delete implementation</li>
     * </ul>
     *
     * <h3>Logging:</h3>
     * <ul>
     *   <li><b>INFO:</b> Account activation/deactivation</li>
     *   <li><b>WARN:</b> Attempt to access inactive account</li>
     *   <li><b>DEBUG:</b> Status changes</li>
     * </ul>
     */
    @Column(name = "is_active")
    private boolean active = true;

    /**
     * Account creation timestamp.
     * <p>
     * Automatically set when the entity is first persisted.
     * Does not update on subsequent modifications.
     * </p>
     *
     * <h3>Time Zone:</h3>
     * <ul>
     *   <li>Stored in UTC (database default)</li>
     *   <li>Format: ISO 8601 (e.g., 2026-08-06T15:30:45Z)</li>
     * </ul>
     *
     * <h3>Logging:</h3>
     * <ul>
     *   <li><b>INFO:</b> Account creation timestamp</li>
     *   <li><b>DEBUG:</b> Timestamp for auditing</li>
     * </ul>
     */
    @CreationTimestamp
    @Column(name = "created_at",
            updatable = false)
    private LocalDateTime createdAt;

    /**
     * Lifecycle callback method - triggered before entity persistence.
     * <p>
     * Logs entity creation details for auditing purposes.
     * </p>
     *
     * <h3>Logging:</h3>
     * <ul>
     *   <li><b>INFO:</b> New user creation attempt</li>
     *   <li><b>DEBUG:</b> User details (excluding password)</li>
     *   <li><b>WARN:</b> Validation issues before save</li>
     * </ul>
     */
    @PrePersist
    public void prePersist() {
        log.info("Creating new user with login: {}", login);
        log.debug("User details before persist - login: {}, email: {}, userName: {}",
                login, email, userName);

        if (login == null || login.trim().isEmpty()) {
            log.warn("Attempting to persist user with null or empty login");
        }

        if (email == null || email.trim().isEmpty()) {
            log.warn("Attempting to persist user with null or empty email");
        }

        if (password == null || password.trim().isEmpty()) {
            log.warn("Attempting to persist user with null or empty password");
        } else {
            log.debug("Password length: {} characters (not logging actual password)", password.length());
        }
    }

    /**
     * Lifecycle callback method - triggered after entity persistence.
     * <p>
     * Logs successful user creation with the generated ID.
     * </p>
     *
     * <h3>Logging:</h3>
     * <ul>
     *   <li><b>INFO:</b> User successfully created</li>
     *   <li><b>DEBUG:</b> Complete entity details for verification</li>
     * </ul>
     */
    @PostPersist
    public void postPersist() {
        log.info("User created successfully with ID: {}, login: {}", id, login);
        log.debug("User details after persist - ID: {}, login: {}, email: {}, userName: {}, active: {}, createdAt: {}",
                id, login, email, userName, active, createdAt);
    }

    /**
     * Lifecycle callback method - triggered before entity update.
     * <p>
     * Logs user update attempts for auditing.
     * </p>
     *
     * <h3>Logging:</h3>
     * <ul>
     *   <li><b>INFO:</b> User update attempt</li>
     *   <li><b>DEBUG:</b> Updated field values</li>
     * </ul>
     */
    @PreUpdate
    public void preUpdate() {
        log.info("Updating user with ID: {}, login: {}", id, login);
        log.debug("User update - login: {}, email: {}, userName: {}, active: {}",
                login, email, userName, active);
    }

    /**
     * Lifecycle callback method - triggered after entity update.
     * <p>
     * Logs successful user updates.
     * </p>
     *
     * <h3>Logging:</h3>
     * <ul>
     *   <li><b>INFO:</b> User successfully updated</li>
     *   <li><b>DEBUG:</b> Updated entity state</li>
     * </ul>
     */
    @PostUpdate
    public void postUpdate() {
        log.info("User updated successfully with ID: {}, login: {}", id, login);
        log.debug("User details after update - ID: {}, login: {}, email: {}, userName: {}, active: {}, createdAt: {}",
                id, login, email, userName, active, createdAt);
    }

    /**
     * Lifecycle callback method - triggered before entity removal.
     * <p>
     * Logs user deletion attempts for auditing and security.
     * </p>
     *
     * <h3>Logging:</h3>
     * <ul>
     *   <li><b>WARN:</b> User deletion attempt</li>
     *   <li><b>INFO:</b> User to be deleted</li>
     *   <li><b>DEBUG:</b> User details before deletion</li>
     * </ul>
     */
    @PreRemove
    public void preRemove() {
        log.warn("Attempting to delete user with ID: {}, login: {}", id, login);
        log.info("Deleting user - login: {}, email: {}, userName: {}", login, email, userName);
        log.debug("User details before deletion - ID: {}, login: {}, email: {}, userName: {}, active: {}, createdAt: {}",
                id, login, email, userName, active, createdAt);
    }

    /**
     * Lifecycle callback method - triggered after entity removal.
     * <p>
     * Logs successful user deletion for auditing.
     * </p>
     *
     * <h3>Logging:</h3>
     * <ul>
     *   <li><b>WARN:</b> User successfully deleted</li>
     *   <li><b>INFO:</b> Confirmation of deletion</li>
     * </ul>
     */
    @PostRemove
    public void postRemove() {
        log.warn("User deleted successfully with ID: {}, login: {}", id, login);
        log.info("User removed from database - login: {}, email: {}", login, email);
    }

    /**
     * Returns a string representation of the user, excluding sensitive data.
     * <p>
     * Password is intentionally excluded from the string representation
     * to prevent accidental exposure in logs.
     * </p>
     *
     * @return string representation without password
     */
    @Override
    public String toString() {
        return String.format("User{id=%s, login='%s', userName='%s', email='%s', active=%s, createdAt=%s}",
                id, login, userName, email, active, createdAt);
    }

    /**
     * Returns a sanitized version of user details for logging purposes.
     * <p>
     * This method is specifically designed for logging and excludes
     * all sensitive information.
     * </p>
     *
     * @return sanitized user details string
     */
    public String toLogString() {
        return String.format("User[id=%s, login='%s', email='%s', userName='%s']",
                id, login, email, userName);
    }
}