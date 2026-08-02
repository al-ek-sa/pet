package org.example.commonmodule.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for user information.
 * <p>
 * This DTO is used for transferring user data between the API layer
 * and the business logic layer. It contains user identification and
 * authentication fields, excluding any sensitive data like passwords.
 * </p>
 *
 * <p><b>Fields:</b></p>
 * <ul>
 *   <li>{@code id} - Unique user identifier (auto-generated UUID)</li>
 *   <li>{@code userName} - Display name for the user interface</li>
 *   <li>{@code login} - Unique username used for authentication</li>
 *   <li>{@code email} - User's email address for communication</li>
 * </ul>
 *
 * <p><b>Usage example:</b></p>
 * <pre>{@code
 * UserDTO user = UserDTO.builder()
 *         .userName("John Doe")
 *         .login("john_doe")
 *         .email("john@example.com")
 *         .build();
 * }</pre>
 *
 * @author Aliaksandra Lishyk
 * @version 1.0
 * @since 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDTO {

    /**
     * Unique identifier of the user.
     * <p>
     * This field is auto-generated using {@link UUID#randomUUID()}
     * when the object is created via the {@link Builder}.
     * </p>
     */
    @Builder.Default
    private UUID id = UUID.randomUUID();

    /**
     * Display name of the user.
     * <p>
     * This name is shown in the user interface, comments,
     * posts, and other social interactions.
     * </p>
     * <p>
     * <b>Constraints:</b>
     * <ul>
     *   <li>Cannot be null or empty</li>
     *   <li>Must be between 3 and 50 characters</li>
     * </ul>
     * </p>
     */
    @NotBlank(message = "Display name is required")
    @Size(min = 3, max = 50, message = "Display name must be between 3 and 50 characters")
    private String userName;

    /**
     * Unique username used for authentication.
     * <p>
     * This username is used for login and must be unique
     * across the entire system.
     * </p>
     * <p>
     * <b>Constraints:</b>
     * <ul>
     *   <li>Cannot be null or empty</li>
     *   <li>Must be between 8 and 50 characters</li>
     * </ul>
     * </p>
     */
    @NotBlank(message = "Login is required")
    @Size(min = 8, max = 50, message = "Login must be between 8 and 50 characters")
    private String login;

    /**
     * Email address of the user.
     * <p>
     * Used for notifications, password recovery, and
     * system communication.
     * </p>
     * <p>
     * <b>Constraints:</b>
     * <ul>
     *   <li>Must be a valid email format (optional)</li>
     * </ul>
     * </p>
     */
    @Email(message = "Invalid email format")
    private String email;
}