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

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody @Valid UserRegistrationDTO registrationDTO) {
        try {
            String token;
            if (registrationDTO instanceof AdminRegistrationDTO) {
                token = authenticationService.registerAdmin((AdminRegistrationDTO) registrationDTO);
            } else if (registrationDTO instanceof CustomerRegistrationDTO) {
                token = authenticationService.registerCustomer((CustomerRegistrationDTO) registrationDTO);
            } else if (registrationDTO instanceof DriverRegistrationDTO) {
                token = authenticationService.registerDriver((DriverRegistrationDTO) registrationDTO);
            } else {
                throw new UnsupportedRegistrationTypeException("Unsupported registration type");
            }
            return ResponseEntity.ok(new AuthResponse(token, "Registration successful"));
        } catch (DuplicateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new AuthResponse(null, e.getMessage()));
        }
    }

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

    public record AuthResponse(String token, String message) {
    }

    public record LoginRequest(@NotBlank String email, @NotBlank String password) {
    }

    public record RefreshTokenRequest(@NotBlank String token) {
    }
}