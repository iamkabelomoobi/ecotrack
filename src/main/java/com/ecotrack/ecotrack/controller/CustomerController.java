package com.ecotrack.ecotrack.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecotrack.ecotrack.config.security.resolver.CurrentUserId;
import com.ecotrack.ecotrack.dto.CustomerDTO;
import com.ecotrack.ecotrack.dto.UserDTO;
import com.ecotrack.ecotrack.dto.AddressDTO;
import com.ecotrack.ecotrack.service.CustomerService;
import com.ecotrack.ecotrack.service.UserService;

import java.util.List;

/**
 * Controller for managing customer-related operations.
 */
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final UserService userService;

    /**
     * Get the customer details for the currently authenticated user.
     *
     * @param userId The ID of the current user, extracted from the JWT.
     * @return The customer details or a 404 response if not found.
     */
    @GetMapping("/me")
    public ResponseEntity<CustomerDTO> getCustomerByUserId(@CurrentUserId Long userId) {
        return customerService.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all customers with optional pagination.
     *
     * @param page The page number (default is 0).
     * @param size The page size (default is 10).
     * @return A list of all customers.
     */
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.findAll());
    }

    /**
     * Update the customer details for the currently authenticated user.
     *
     * @param userId      The ID of the current user, extracted from the JWT.
     * @param customerDTO The updated customer details.
     * @return The updated customer details.
     */
    @PutMapping("/me")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @CurrentUserId Long userId,
            @Valid @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(
                customerService.updateCustomerByUserId(userId, customerDTO));
    }

    /**
     * Update the contact details (email and phone) for the currently authenticated
     * user.
     *
     * @param userId The ID of the current user, extracted from the JWT.
     * @param email  The new email address (optional).
     * @param phone  The new phone number (optional).
     * @return The updated user details.
     */
    @PatchMapping("/me/contact")
    public ResponseEntity<UserDTO> updateContactDetails(
            @CurrentUserId Long userId,
            @RequestParam(required = false) @Email String email,
            @RequestParam(required = false) @Pattern(regexp = "^\\+?[0-9]{10,15}$") String phone) {

        userService.updateUserContactDetails(userId, email, phone);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update the password for the currently authenticated user.
     *
     * @param userId  The ID of the current user, extracted from the JWT.
     * @param request The password change request containing the current and new
     *                passwords.
     * @return A 204 No Content response if the password was successfully updated.
     */
    @PostMapping("/me/password")
    public ResponseEntity<Void> updatePassword(
            @CurrentUserId Long userId,
            @Valid @RequestBody PasswordChangeRequest request) {
        if (request.getCurrentPassword() == null || request.getNewPassword() == null) {
            throw new IllegalArgumentException("Both current and new passwords are required.");
        }

        userService.updatePassword(userId, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    /**
     * Create or update the address for the currently authenticated user.
     *
     * @param userId     The ID of the current user, extracted from the JWT.
     * @param addressDTO The address details to create or update.
     * @return The created or updated address details.
     */
    @PostMapping("/me/address")
    public ResponseEntity<AddressDTO> createOrUpdateAddress(
            @CurrentUserId Long userId,
            @Valid @RequestBody AddressDTO addressDTO) {
        return ResponseEntity.ok(customerService.createOrUpdateAddress(userId, addressDTO));
    }

    /**
     * Get the address for the currently authenticated user.
     *
     * @param userId The ID of the current user, extracted from the JWT.
     * @return The address details.
     */
    @GetMapping("/me/address")
    public ResponseEntity<AddressDTO> getAddress(@CurrentUserId Long userId) {
        return ResponseEntity.ok(customerService.getAddress(userId));
    }

    /**
     * Delete the address for the currently authenticated user.
     *
     * @param userId The ID of the current user, extracted from the JWT.
     * @return A 204 No Content response if the address was successfully deleted.
     */
    @DeleteMapping("/me/address")
    public ResponseEntity<Void> deleteAddress(@CurrentUserId Long userId) {
        customerService.deleteAddress(userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Request object for changing a user's password.
     */
    @Data
    public static class PasswordChangeRequest {
        @NotBlank(message = "Current password is required")
        private String currentPassword;

        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$", message = "Password must contain at least one digit, one lowercase and one uppercase letter")
        private String newPassword;
    }
}