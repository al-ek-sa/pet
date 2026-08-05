package org.example.authservice.bd.repository;

import org.example.authservice.bd.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for {@link User} entity operations.
 * <p>
 * This interface provides custom database queries using native SQL.
 * All methods use native queries for direct interaction with the "users" table.
 * </p>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Native SQL queries for optimized database access</li>
 *   <li>Parameterized queries to prevent SQL injection</li>
 *   <li>Support for unique lookups (login, id)</li>
 *   <li>Support for partial matching (userName with LIKE)</li>
 *   <li>Composite query for login and email combination</li>
 * </ul>
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 * @see User
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Finds a user by their login using native SQL query.
     * <p>
     * Performs an exact match search on the "login" column.
     * Returns an empty Optional if no user is found.
     * </p>
     *
     * @param login the unique login of the user to search for
     * @return an {@link Optional} containing the found user, or empty if not found
     */
    //todo
    @Query(value = "SELECT * FROM users WHERE login = :login", nativeQuery = true)
    Optional<User> findByLoginNative(@Param("login") String login);

    /**
     * Finds users whose display name contains the given substring.
     * <p>
     * Performs a case-sensitive partial match using SQL LIKE operator.
     * Returns all users whose "user_name" field contains the specified text.
     * </p>
     *
     * @param userName the substring to search for in user names
     * @return a {@link List} of users matching the search criteria,
     *         empty list if no matches found
     */
    //todo
    @Query(value = "SELECT id FROM users WHERE user_name LIKE %:user_name%", nativeQuery = true)
    List<UUID> findByUserNameNative(@Param("user_name") String userName);

    /**
     * Finds a user by both login and email combination.
     * <p>
     * Useful for verification scenarios where both credentials must match.
     * Performs an exact match on both fields simultaneously.
     * </p>
     *
     * @param login the user's login
     * @param email the user's email address
     * @return an {@link Optional} containing the user if both login and email match,
     *         empty if no user found with the specified combination
     */
    //todo
    @Query(value = "SELECT * FROM users WHERE login = :login and email = :email", nativeQuery = true)
    Optional<User> findByLoginAndEmailNative(@Param("login") String login,
                                             @Param("email") String email);
}