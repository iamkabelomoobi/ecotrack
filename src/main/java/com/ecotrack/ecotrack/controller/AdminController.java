package com.ecotrack.ecotrack.controller;

import com.ecotrack.ecotrack.config.security.resolver.CurrentUserId;
import com.ecotrack.ecotrack.dto.AdminDTO;
import com.ecotrack.ecotrack.dto.ContactDetailsDTO;
import com.ecotrack.ecotrack.service.AdminService;
import com.ecotrack.ecotrack.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    /**
     * Get the admin details for the currently authenticated user.
     *
     * @param userId The ID of the current user, extracted from the JWT.
     * @return The admin details or a 404 response if not found.
     */
    @GetMapping("/me")
    public ResponseEntity<AdminDTO> getAdminByUserId(@CurrentUserId Long userId) {
        return adminService.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all admins with optional pagination.
     *
     * @param page The page number (default is 0).
     * @param size The page size (default is 10).
     * @return A paginated list of admins.
     */
    @GetMapping
    public ResponseEntity<List<AdminDTO>> getAllAdmins() {
        return ResponseEntity.ok(adminService.findAll());
    }

    /**
     * Update the admin details for the currently authenticated user.
     *
     * @param userId   The ID of the current user, extracted from the JWT.
     * @param adminDTO The updated admin details.
     * @return The updated admin details.
     */
    @PutMapping("/me")
    public ResponseEntity<AdminDTO> updateAdmin(
            @CurrentUserId Long userId,
            @Valid @RequestBody AdminDTO adminDTO) {
        return ResponseEntity.ok(
                adminService.update(userId, adminDTO));
    }

    /**
     * Updates the contact details (email and/or phone) for the currently
     * authenticated user.
     *
     * @param userId         The ID of the current user, extracted from the JWT.
     * @param contactDetails The new contact details containing email and phone.
     * @return A ResponseEntity with no content if the update is successful.
     */
    @PatchMapping("/me/contact")
    public ResponseEntity<Void> updateAdminContactDetails(
            @CurrentUserId Long userId,
            @Valid @RequestBody ContactDetailsDTO contactDetails) {

        userService.updateUserContactDetails(userId, contactDetails.getEmail(), contactDetails.getPhone());
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates the password for the currently authenticated user.
     *
     * @param userId  The ID of the currently authenticated user, injected
     *                via @CurrentUserId.
     * @param request The password change request containing the current and new
     *                passwords.
     * @return A ResponseEntity with no content if the password update is
     *         successful.
     * @throws IllegalArgumentException If either the current password or the new
     *                                  password is null.
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
