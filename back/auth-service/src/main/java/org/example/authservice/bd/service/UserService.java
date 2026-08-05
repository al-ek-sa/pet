package org.example.authservice.bd.service;

import lombok.RequiredArgsConstructor;
import org.example.authservice.bd.repository.UserRepository;
import org.example.authservice.bd.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing user-related business operations.
 * <p>
 * This service provides CRUD operations and custom queries for User entities.
 * All methods that modify data are marked as {@code @Transactional} to ensure
 * data consistency and integrity.
 * </p>
 *
 * <h2>Key Features:</h2>
 * <ul>
 *   <li>Complete CRUD operations (Create, Read, Update, Delete)</li>
 *   <li>Transaction management for data consistency</li>
 *   <li>Custom query for finding users by login and email</li>
 *   <li>Uses dependency injection via {@code @RequiredArgsConstructor}</li>
 * </ul>
 *
 * <h2>Transaction Management:</h2>
 * <ul>
 *   <li>All write operations are wrapped in transactions</li>
 *   <li>Read operations are also transactional for consistency</li>
 *   <li>Spring handles transaction boundaries automatically</li>
 * </ul>
 *
 * <h2>Usage Examples:</h2>
 *
 * <p><b>Save a new user:</b></p>
 * <pre>
 * User user = User.builder()
 *     .login("john_doe")
 *     .password(encodedPassword)
 *     .userName("John Doe")
 *     .email("john@example.com")
 *     .active(true)
 *     .build();
 * userService.save(user);
 * </pre>
 *
 * <p><b>Find user by login and email:</b></p>
 * <pre>
 * Optional&lt;User&gt; user = userService.findByLoginAndEmail("john_doe", "john@example.com");
 * user.ifPresent(u -> System.out.println("User found: " + u.getUserName()));
 * </pre>
 *
 * <p><b>Remove all users:</b></p>
 * <pre>
 * userService.removeAll(); // USE WITH CAUTION!
 * </pre>
 *
 * <h2>Important Security Notes:</h2>
 * <ul>
 *   <li>Passwords should be encoded before calling save()</li>
 *   <li>Consider adding validation before save operations</li>
 *   <li>removeAll() should be used carefully in production</li>
 * </ul>
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 * @see org.example.authservice.bd.entity.User
 * @see org.example.authservice.bd.repository.UserRepository
 * @see org.springframework.transaction.annotation.Transactional
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Saves a user to the database.
     * <p>
     * Persists the given User entity. If the user already exists (based on ID),
     * it will be updated; otherwise, a new record will be created.
     * </p>
     *
     * <h3>Preconditions:</h3>
     * <ul>
     *   <li>User object must not be null</li>
     *   <li>Login must be unique (handled by repository)</li>
     *   <li>Email must be unique (handled by repository)</li>
     *   <li>Password should be encoded</li>
     * </ul>
     *
     * <h3>Exceptions:</h3>
     * <ul>
     *   <li>May throw {@code DataIntegrityViolationException} if constraints violated</li>
     *   <li>May throw {@code IllegalArgumentException} if user is null</li>
     * </ul>
     *
     * <h3>Transaction:</h3>
     * <p>This method is transactional, meaning any database failure will cause a rollback.</p>
     *
     * @param user the User entity to save (must not be null)
     * @throws IllegalArgumentException if user is null
     * @see org.springframework.dao.DataIntegrityViolationException
     */
    @Transactional
    public void save(User user){
        userRepository.save(user);
    }

    /**
     * Removes a specific user from the database.
     * <p>
     * Deletes the given User entity. If the user doesn't exist,
     * this operation will throw an exception.
     * </p>
     *
     * <h3>Important Notes:</h3>
     * <ul>
     *   <li>The user must exist in the database</li>
     *   <li>Deletion is permanent and cannot be undone</li>
     *   <li>Consider implementing soft delete instead for production</li>
     * </ul>
     *
     * <h3>Transaction:</h3>
     * <p>This method is transactional for data consistency.</p>
     *
     * @param user the User entity to delete (must exist in database)
     * @throws org.springframework.dao.EmptyResultDataAccessException if user not found
     */
    @Transactional
    public void remove(User user){
        userRepository.delete(user);
    }

    /**
     * Removes all users from the database.
     * <p>
     * <b>WARNING:</b> This operation is irreversible and will delete
     * all user records. Use with extreme caution in production environments.
     * </p>
     *
     * <h3>Use Cases:</h3>
     * <ul>
     *   <li>Testing environments</li>
     *   <li>Development database cleanup</li>
     *   <li>System reset operations</li>
     * </ul>
     *
     * <h3>Performance Impact:</h3>
     * <p>This operation may be expensive on large datasets and can lock the table.</p>
     *
     * <h3>Transaction:</h3>
     * <p>This method is transactional - all users will be deleted or none.</p>
     */
    @Transactional
    public void removeAll(){
        userRepository.deleteAll();
    }

    /**
     * Retrieves all users from the database.
     * <p>
     * Returns a list of all registered users. This method does not apply
     * any filtering or pagination.
     * </p>
     *
     * <h3>Performance Considerations:</h3>
     * <ul>
     *   <li>For large datasets, consider implementing pagination</li>
     *   <li>Returns all fields including sensitive data (e.g., password)</li>
     *   <li>Consider using projections or DTOs to limit returned data</li>
     * </ul>
     *
     * <h3>Transaction:</h3>
     * <p>This method is transactional to ensure consistent reads.</p>
     *
     * @return List of all User entities (may be empty if no users exist)
     * @see org.springframework.data.domain.Pageable for pagination support
     */
    @Transactional
    public List<User> findAll(){
        return userRepository.findAll();
    }

    /**
     * Finds a user by their login and email using a native SQL query.
     * <p>
     * Performs a case-sensitive search for a user matching both the login
     * and email address. Returns an Optional containing the user if found.
     * </p>
     *
     * <h3>Search Criteria:</h3>
     * <ul>
     *   <li><b>login:</b> Exact match (case-sensitive)</li>
     *   <li><b>email:</b> Exact match (case-sensitive)</li>
     *   <li>Both conditions must be satisfied simultaneously</li>
     * </ul>
     *
     * <h3>Use Cases:</h3>
     * <ul>
     *   <li>Account recovery verification</li>
     *   <li>Security checks during login</li>
     *   <li>Validating user identity</li>
     * </ul>
     *
     * <h3>Return Values:</h3>
     * <ul>
     *   <li>{@code Optional.of(user)} - if a user matching both criteria exists</li>
     *   <li>{@code Optional.empty()} - if no matching user found</li>
     * </ul>
     *
     * <h3>Transaction:</h3>
     * <p>This method is transactional for consistent read operations.</p>
     *
     * @param login the user's login (username) to search for
     * @param email the user's email to search for
     * @return Optional containing the found User, or empty if none found
     * @see org.example.authservice.bd.repository.UserRepository#findByLoginAndEmailNative(String, String)
     */
    @Transactional
    public Optional<User> findByLoginAndEmail(String login, String email){
        return userRepository.findByLoginAndEmailNative(login, email);
    }
}