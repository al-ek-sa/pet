package org.example.authservice.bd.controller;

import lombok.RequiredArgsConstructor;
import org.example.authservice.bd.entity.User;
import org.example.authservice.bd.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing user operations in the authentication service.
 * <p>
 * Provides endpoints for creating new users and retrieving user information.
 * All endpoints are prefixed with {@code /api/users}.
 * </p>
 *
 * <p><b>Base URL:</b> {@code /api/users}</p>
 *
 * <h2>Available Endpoints:</h2>
 * <ul>
 *   <li><b>POST /api/users</b> - Create a new user</li>
 *   <li><b>GET /api/users</b> - Retrieve all users</li>
 * </ul>
 *
 * <h2>Features:</h2>
 * <ul>
 *   <li>Simple CRUD operations for User entity</li>
 *   <li>Uses dependency injection via {@code @RequiredArgsConstructor}</li>
 *   <li>Returns simple string responses for operation status</li>
 * </ul>
 *
 * <p><b>Note:</b> This is a basic controller. For production use, consider adding:</p>
 * <ul>
 *   <li>Response status codes (201 Created, 200 OK, 400 Bad Request)</li>
 *   <li>Request validation using {@code @Valid}</li>
 *   <li>Exception handling with {@code @ControllerAdvice}</li>
 *   <li>Pagination for GET endpoint</li>
 *   <li>DTOs instead of exposing entity directly</li>
 * </ul>
 *
 * <h2>Usage Examples:</h2>
 *
 * <p><b>Create a new user:</b></p>
 * <pre>
 * POST /api/users
 * Content-Type: application/json
 *
 * {
 *   "login": "john_doe",
 *   "password": "securePass123",
 *   "userName": "John Doe",
 *   "email": "john@example.com",
 *   "active": true
 * }
 * </pre>
 * <p><b>Response:</b> "User added successfully!"</p>
 *
 * <p><b>Get all users:</b></p>
 * <pre>
 * GET /api/users
 * </pre>
 * <p><b>Response:</b> JSON array of all users</p>
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 * @see org.example.authservice.bd.entity.User
 * @see org.example.authservice.bd.service.UserService
 * @see org.springframework.web.bind.annotation.RestController
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Creates a new user in the system.
     * <p>
     * Accepts a User object in the request body and persists it to the database.
     * </p>
     *
     * <h3>Request Body Requirements:</h3>
     * <ul>
     *   <li><b>login:</b> Required, unique, max 50 chars</li>
     *   <li><b>password:</b> Required, should be encoded before sending</li>
     *   <li><b>userName:</b> Required, max 100 chars</li>
     *   <li><b>email:</b> Required, unique, valid email format</li>
     *   <li><b>active:</b> Optional, defaults to true</li>
     * </ul>
     *
     * <h3>Important Security Note:</h3>
     * <p>Passwords should be encoded using BCrypt or similar before saving.
     * Consider using {@code @Valid} annotation for input validation.</p>
     *
     * @param user the User object to be created (from request body)
     * @return success message as a string
     *
     * <p><b>Potential improvements:</b></p>
     * <ul>
     *   <li>Return 201 Created with user ID instead of simple message</li>
     *   <li>Add validation for password strength</li>
     *   <li>Check for duplicate login/email before saving</li>
     * </ul>
     */
    @PostMapping
    public String addUser(@RequestBody User user) {
        userService.save(user);
        return "User added successfully!";
    }

    /**
     * Retrieves a list of all registered users.
     * <p>
     * Returns all users from the database. In production, consider
     * implementing pagination to handle large datasets.
     * </p>
     *
     * <h3>Response Format:</h3>
     * <p>Returns a JSON array of User objects. Password field is included
     * but should be filtered out in production for security.</p>
     *
     * <h3>Example Response:</h3>
     * <pre>
     * [
     *   {
     *     "id": "550e8400-e29b-41d4-a716-446655440000",
     *     "login": "john_doe",
     *     "password": "$2a$10$encrypted...",
     *     "userName": "John Doe",
     *     "email": "john@example.com",
     *     "active": true,
     *     "createdAt": "2026-08-06T15:30:45Z"
     *   }
     * ]
     * </pre>
     *
     * @return list of all User entities
     *
     * <p><b>Security recommendation:</b></p>
     * <ul>
     *   <li>Exclude password field from response</li>
     *   <li>Add authentication/authorization</li>
     *   <li>Implement pagination for performance</li>
     * </ul>
     */
    @GetMapping
    public List<User> findAllUsers(){
        return userService.findAll();
    }
}