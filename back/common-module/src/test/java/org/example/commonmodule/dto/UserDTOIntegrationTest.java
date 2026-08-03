package org.example.commonmodule.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link UserDTO} validation constraints.
 * <p>
 * This test class verifies that all validation annotations
 * ({@code @NotBlank}, {@code @Size}, {@code @Email}) work correctly
 * for the UserDTO fields.
 * </p>
 *
 * <p><b>Test coverage:</b></p>
 * <ul>
 *   <li>Valid user data — all constraints satisfied</li>
 *   <li>Invalid {@code userName} — null, empty, too short, too long</li>
 *   <li>Invalid {@code login} — null, too short</li>
 *   <li>Invalid {@code email} — invalid format</li>
 *   <li>Null {@code email} — allowed (optional field)</li>
 *   <li>Multiple violations — all reported simultaneously</li>
 * </ul>
 *
 * @author Aliaksandra Lishyk
 * @version 1.0
 */
class UserDTOTest {

    private ValidatorFactory factory;
    private Validator validator;

    /**
     * Initializes the Javax Validation Validator before each test.
     * <p>
     * The validator is created using the default validation factory
     * and is used to validate {@link UserDTO} instances.
     * </p>
     */
    @BeforeEach
    void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * Closes the ValidatorFactory after each test to release resources.
     */
    @AfterEach
    void tearDown() {
        if (factory != null) {
            factory.close();
        }
    }

    /**
     * Tests that a valid UserDTO with all fields correctly filled
     * passes validation without any constraint violations.
     */
    @Test
    void whenAllFieldsValid_thenNoViolations() {
        // Given
        UserDTO user = new UserDTO();
        user.setUserName("John Doe");
        user.setLogin("john_doe");
        user.setEmail("john@example.com");

        // When
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        // Then
        assertTrue(violations.isEmpty(), "No violations expected");
    }

    /**
     * Tests that validation fails when the {@code userName}
     * field is null.
     */
    @Test
    void whenUserNameIsNull_thenViolation() {
        // Given
        UserDTO user = new UserDTO();
        user.setUserName(null);
        user.setLogin("john_doe");
        user.setEmail("john@example.com");

        // When
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        ConstraintViolation<UserDTO> violation = violations.iterator().next();
        assertEquals("userName", violation.getPropertyPath().toString());
        assertEquals("Display name is required", violation.getMessage());
    }

    /**
     * Tests that validation fails when the {@code userName}
     * field is an empty string.
     * <p>
     * Note: The violation message may come from either {@code @NotBlank}
     * or {@code @Size} constraints. We only verify that a violation exists
     * for the userName field, not the specific message.
     * </p>
     */
    @Test
    void whenUserNameIsEmpty_thenViolation() {
        // Given
        UserDTO user = new UserDTO();
        user.setUserName("");
        user.setLogin("john_doe");
        user.setEmail("john@example.com");

        // When
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        // Then
        assertFalse(violations.isEmpty());
        ConstraintViolation<UserDTO> violation = violations.iterator().next();
        assertEquals("userName", violation.getPropertyPath().toString());
        // ✅ НЕ ПРОВЕРЯЕМ СООБЩЕНИЕ!
    }

    /**
     * Tests that validation fails when the {@code userName}
     * field is shorter than the minimum length of 3 characters.
     */
    @Test
    void whenUserNameIsTooShort_thenViolation() {
        // Given
        UserDTO user = new UserDTO();
        user.setUserName("Jo");
        user.setLogin("john_doe");
        user.setEmail("john@example.com");

        // When
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        // Then
        assertFalse(violations.isEmpty());
        ConstraintViolation<UserDTO> violation = violations.iterator().next();
        assertEquals("userName", violation.getPropertyPath().toString());
        assertEquals("Display name must be between 3 and 50 characters", violation.getMessage());
    }

    /**
     * Tests that validation fails when the {@code userName}
     * field exceeds the maximum length of 50 characters.
     */
    @Test
    void whenUserNameIsTooLong_thenViolation() {
        // Given
        UserDTO user = new UserDTO();
        user.setUserName("A".repeat(51));
        user.setLogin("john_doe");
        user.setEmail("john@example.com");

        // When
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        // Then
        assertFalse(violations.isEmpty());
        ConstraintViolation<UserDTO> violation = violations.iterator().next();
        assertEquals("userName", violation.getPropertyPath().toString());
        assertEquals("Display name must be between 3 and 50 characters", violation.getMessage());
    }

    /**
     * Tests that validation fails when the {@code login}
     * field is null.
     */
    @Test
    void whenLoginIsNull_thenViolation() {
        // Given
        UserDTO user = new UserDTO();
        user.setUserName("John Doe");
        user.setLogin(null);
        user.setEmail("john@example.com");

        // When
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        // Then
        assertFalse(violations.isEmpty());
        ConstraintViolation<UserDTO> violation = violations.iterator().next();
        assertEquals("login", violation.getPropertyPath().toString());
        assertEquals("Login is required", violation.getMessage());
    }

    /**
     * Tests that validation fails when the {@code login}
     * field is shorter than the minimum length of 8 characters.
     */
    @Test
    void whenLoginIsTooShort_thenViolation() {
        // Given
        UserDTO user = new UserDTO();
        user.setUserName("John Doe");
        user.setLogin("john");
        user.setEmail("john@example.com");

        // When
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        // Then
        assertFalse(violations.isEmpty());
        ConstraintViolation<UserDTO> violation = violations.iterator().next();
        assertEquals("login", violation.getPropertyPath().toString());
        assertEquals("Login must be between 8 and 50 characters", violation.getMessage());
    }

    /**
     * Tests that validation fails when the {@code email}
     * field has an invalid format.
     */
    @Test
    void whenEmailIsInvalid_thenViolation() {
        // Given
        UserDTO user = new UserDTO();
        user.setUserName("John Doe");
        user.setLogin("john_doe");
        user.setEmail("invalid-email");

        // When
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        // Then
        assertFalse(violations.isEmpty());
        ConstraintViolation<UserDTO> violation = violations.iterator().next();
        assertEquals("email", violation.getPropertyPath().toString());
        assertEquals("Invalid email format", violation.getMessage());
    }

    /**
     * Tests that a null {@code email} field does not cause
     * any validation violations because it is optional.
     */
    @Test
    void whenEmailIsNull_thenNoViolationForEmail() {
        // Given
        UserDTO user = new UserDTO();
        user.setUserName("John Doe");
        user.setLogin("john_doe");
        user.setEmail(null);

        // When
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        // Then
        assertTrue(violations.isEmpty(), "Email can be null, no violation");
    }

    /**
     * Tests that when multiple fields are invalid,
     * all constraint violations are reported simultaneously.
     */
    @Test
    void whenMultipleViolations_thenAllReported() {
        // Given — all fields invalid
        UserDTO user = new UserDTO();
        user.setUserName(null);      // violates @NotBlank
        user.setLogin("abc");        // violates @Size(min=8)
        user.setEmail("invalid");    // violates @Email

        // When
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        // Then — three violations reported at once
        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size());
    }
}