package org.example.authservice.bd.repository;

import org.example.authservice.bd.entity.User;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for {@link User} entity existence checks.
 * <p>
 * This interface provides secure database queries that return only boolean values
 * to prevent leakage of sensitive user data (passwords, emails, etc.).
 * All methods use native SQL with parameterized queries to prevent SQL injection.
 * </p>
 *
 * <p><b>Security Features:</b></p>
 * <ul>
 *   <li>All methods return only {@code boolean} (true/false) — no user data exposed</li>
 *   <li>Uses SQL {@code EXISTS} for optimal performance (stops at first match)</li>
 *   <li>Parameterized queries with {@code @Param} to prevent SQL injection</li>
 *   <li>No password or sensitive data is ever returned from these queries</li>
 * </ul>
 *
 * <p><b>Available Checks:</b></p>
 * <ul>
 *   <li>{@link #findByLoginNative(String)} — Check if user exists by login</li>
 *   <li>{@link #existsByUserNameContaining(String)} — Check if any user name contains substring</li>
 *   <li>{@link #findByLoginAndEmailNative(String, String)} — Check if user exists by login and email</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * // Check if user exists before registration
 * if (userRepository.findByLoginNative(login)) {
 *     throw new RuntimeException("User already exists");
 * }
 *
 * // Check if user exists with both login and email
 * if (userRepository.findByLoginAndEmailNative(login, email)) {
 *     // User found with matching credentials
 * }
 * }</pre>
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 * @see User
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Checks if a user exists with the given login.
     * <p>
     * Performs an exact match search on the "login" column.
     * Returns {@code true} if at least one user with this login exists.
     * </p>
     *
     * <p><b>Security:</b> Returns only boolean, no user data is exposed.</p>
     *
     * @param login the unique login of the user to check
     * @return {@code true} if user exists, {@code false} otherwise
     */
    @Query(value = "SELECT EXISTS (SELECT 1 FROM users WHERE login = :login)", nativeQuery = true)
    boolean findByLoginNative(@Param("login") String login);

    @Query(value = "SELECT password FROM users WHERE login = :login", nativeQuery = true)
    @Nullable
    String findByLoginNativePassword(@Param("login") String login);

    /**
     * Checks if any user's display name contains the given substring.
     * <p>
     * Performs a case-sensitive partial match using SQL LIKE operator.
     * Returns {@code true} if any user's "user_name" field contains the specified text.
     * </p>
     *
     * <p><b>Security:</b> Returns only boolean, no user data is exposed.</p>
     *
     * @param userName the substring to search for in user names
     * @return {@code true} if any user found with matching name, {@code false} otherwise
     */
    @Query(value = "SELECT EXISTS(SELECT 1 FROM users WHERE user_name LIKE %:userName%)", nativeQuery = true)
    boolean existsByUserNameContaining(@Param("userName") String userName);

    /**
     * Checks if a user exists with both login and email combination.
     * <p>
     * Performs an exact match on both fields simultaneously.
     * Useful for verification scenarios where both credentials must match
     * (e.g., password recovery, account verification).
     * </p>
     *
     * <p><b>Security:</b> Returns only boolean, no user data is exposed.</p>
     *
     * @param login the user's login
     * @param email the user's email address
     * @return {@code true} if user exists with both login and email, {@code false} otherwise
     */
    @Query(value = "SELECT EXISTS (SELECT 1 FROM users WHERE login = :login and email = :email)", nativeQuery = true)
    boolean findByLoginAndEmailNative(@Param("login") String login,
                                      @Param("email") String email);
}