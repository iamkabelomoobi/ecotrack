package com.ecotrack.ecotrack.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecotrack.ecotrack.dto.*;
import com.ecotrack.ecotrack.exception.*;
import com.ecotrack.ecotrack.service.AuthenticationService;

/**
 * Controller for managing authentication-related operations.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    /**
     * Registers a new admin user.
     *
     * @param registrationDTO The registration details for the admin.
     * @return A response containing the authentication token and a success message.
     */
    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody @Valid AdminRegistrationDTO registrationDTO) {
        try {
            String token = authenticationService.registerAdmin(registrationDTO);
            return ResponseEntity.ok(new AuthResponse(token, "Admin registration successful"));
        } catch (DuplicateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new AuthResponse(null, e.getMessage()));
        }
    }

    /**
     * Registers a new customer user.
     *
     * @param registrationDTO The registration details for the customer.
     * @return A response containing the authentication token and a success message.
     */
    @PostMapping("/register/customer")
    public ResponseEntity<AuthResponse> registerCustomer(@RequestBody @Valid CustomerRegistrationDTO registrationDTO) {
        try {
            String token = authenticationService.registerCustomer(registrationDTO);
            return ResponseEntity.ok(new AuthResponse(token, "Customer registration successful"));
        } catch (DuplicateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new AuthResponse(null, e.getMessage()));
        }
    }

    /**
     * Registers a new driver user.
     *
     * @param registrationDTO The registration details for the driver.
     * @return A response containing the authentication token and a success message.
     */
    @PostMapping("/register/driver")
    public ResponseEntity<AuthResponse> registerDriver(@RequestBody @Valid DriverRegistrationDTO registrationDTO) {
        try {
            String token = authenticationService.registerDriver(registrationDTO);
            return ResponseEntity.ok(new AuthResponse(token, "Driver registration successful"));
        } catch (DuplicateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new AuthResponse(null, e.getMessage()));
        }
    }

    /**
     * Authenticates a user and generates a JWT token.
     *
     * @param request The login request containing the user's email and password.
     * @return A response containing the authentication token and a success message.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        try {
            String token = authenticationService.login(request.email(), request.password());
            return ResponseEntity.ok(new AuthResponse(token, "Login successful"));
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, e.getMessage()));
        }
    }

    /**
     * Refreshes an expired JWT token.
     *
     * @param request The refresh token request containing the expired token.
     * @return A response containing the new authentication token and a success
     *         message.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        try {
            String newToken = authenticationService.refreshToken(request.token());
            return ResponseEntity.ok(new AuthResponse(newToken, "Token refreshed successfully"));
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, e.getMessage()));
        }
    }

    /**
     * Response object for authentication-related operations.
     *
     * @param token   The authentication token.
     * @param message A message describing the result of the operation.
     */
    public record AuthResponse(String token, String message) {
    }

    /**
     * Request object for user login.
     *
     * @param email    The user's email address.
     * @param password The user's password.
     */
    public record LoginRequest(@NotBlank String email, @NotBlank String password) {
    }

    /**
     * Request object for refreshing a JWT token.
     *
     * @param token The expired JWT token.
     */
    public record RefreshTokenRequest(@NotBlank String token) {
    }
}