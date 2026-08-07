package org.example.authservice.bd.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authservice.bd.config.SecurityConfigPassword;
import org.example.authservice.bd.entity.User;
import org.example.authservice.bd.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for managing user-related business operations.
 * <p>
 * This service provides secure user management with password encoding and
 * existence checks. All methods are marked as {@code @Transactional} at the class level
 * to ensure data consistency and integrity.
 * </p>
 *
 * <h2>Security Features:</h2>
 * <ul>
 *   <li>Passwords are automatically encoded using Argon2 before saving</li>
 *   <li>Existence checks return only boolean (no data leakage)</li>
 *   <li>Passwords are never logged in any form</li>
 *   <li>Uses parameterized queries to prevent SQL injection</li>
 * </ul>
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 * @see User
 * @see UserRepository
 * @see org.springframework.transaction.annotation.Transactional
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final SecurityConfigPassword securityConfig;

    /**
     * Registers a new user with password encoding.
     * <p>
     * This method automatically encodes the password using Argon2 before saving.
     * It performs existence checks to prevent duplicate registrations.
     * </p>
     *
     * @param user the User entity to register (password can be plain text)
     * @throws RuntimeException if login or email already exists
     */
    public void registerUser(User user) {
        log.info("Registering new user with login: {}", user.getLogin());

        // Secure existence check (returns only boolean)
        if (userRepository.findByLoginNative(user.getLogin())) {
            log.warn("Registration failed - login already exists: {}", user.getLogin());
            throw new RuntimeException("User with login '" + user.getLogin() + "' already exists");
        }

        // Secure existence check (returns only boolean)
        if (userRepository.findByLoginAndEmailNative(user.getLogin(), user.getEmail())) {
            log.warn("Registration failed - user with login and email combination already exists");
            throw new RuntimeException("User with login '" + user.getLogin() + "' and email '" + user.getEmail() + "' already exists");
        }

        // Encode password using Argon2
        String encodedPassword = securityConfig.passwordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);
        log.debug("Password encoded successfully for user: {}", user.getLogin());

        // Save user
        try {
            User savedUser = userRepository.save(user);
            log.info("User registered successfully with ID: {}, login: {}",
                    savedUser.getId(), savedUser.getLogin());
        } catch (Exception e) {
            log.error("Failed to register user with login: {}. Error: {}",
                    user.getLogin(), e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Saves a user (internal use only).
     * <p>
     * <b>Note:</b> This method doesn't check for duplicates.
     * For registration with validation, use {@link #registerUser(User)}.
     * </p>
     *
     * @param user the User entity to save
     */
    public void save(User user) {
        log.info("Saving user with login: {}", user.getLogin());

        try {
            User savedUser = userRepository.save(user);
            log.info("User saved successfully with ID: {}", savedUser.getId());
        } catch (Exception e) {
            log.error("Failed to save user with login: {}. Error: {}", user.getLogin(), e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Checks if a user exists with the given login.
     * <p>
     * <b>Security:</b> Returns only boolean. No user data is exposed.
     * </p>
     *
     * @param login the user's login to check
     * @return {@code true} if user exists, {@code false} otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsByLogin(String login) {
        log.info("Checking existence for login: {}", login);
        boolean exists = userRepository.findByLoginNative(login);
        log.debug("User with login '{}' exists: {}", login, exists);
        return exists;
    }

    /**
     * Checks if any user's name contains the given substring.
     *
     * @param userName the substring to search for
     * @return {@code true} if any user found, {@code false} otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsByUserNameContaining(String userName) {
        log.info("Checking if any user name contains: {}", userName);
        boolean exists = userRepository.existsByUserNameContaining(userName);
        log.debug("User name containing '{}' exists: {}", userName, exists);
        return exists;
    }

    /**
     * Checks if a user exists with both login and email combination.
     *
     * @param login the user's login
     * @param email the user's email
     * @return {@code true} if user exists with both login and email, {@code false} otherwise
     */
    @Transactional(readOnly = true)
    public boolean existsByLoginAndEmail(String login, String email) {
        log.info("Checking existence for login: {} and email: {}", login, email);
        boolean exists = userRepository.findByLoginAndEmailNative(login, email);
        log.debug("User with login '{}' and email '{}' exists: {}", login, email, exists);
        return exists;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return List of all User entities (may be empty if no users exist)
     */
    @Transactional(readOnly = true)
    public List<User> findAll() {
        log.info("Fetching all users from database");

        try {
            List<User> users = userRepository.findAll();

            if (users.isEmpty()) {
                log.warn("No users found in database");
            } else {
                log.info("Successfully retrieved {} users", users.size());
            }

            return users;
        } catch (Exception e) {
            log.error("Failed to fetch users from database. Error: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void remove(User user) {
        log.warn("Attempting to delete user with ID: {}, login: {}", user.getId(), user.getLogin());

        try {
            userRepository.delete(user);
            log.warn("User deleted successfully with ID: {}, login: {}",
                    user.getId(), user.getLogin());
        } catch (Exception e) {
            log.error("Failed to delete user with ID: {}. Error: {}", user.getId(), e.getMessage(), e);
            throw e;
        }
    }
}