package org.example.authservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Unique user identifier.
     * <p>
     * Automatically generated using UUID strategy.
     * Serves as the primary key of the table.
     * </p>
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
     */
    @Column(unique = true,
            nullable = false,
            length = 100,
            name = "email")
    private String email;

    /**
     * Account activity status flag.
     * <p>
     * Allows temporary account blocking without deleting the record.
     * Default value is {@code true} (active).
     * </p>
     */
    @Column(name = "is_active")
    private boolean active = true;

    /**
     * Account creation timestamp.
     * <p>
     * Automatically set when the entity is first persisted.
     * Does not update on subsequent modifications.
     * </p>
     */
    @CreationTimestamp
    @Column(name = "created_at",
            updatable = false)
    private LocalDateTime createdAt;
}