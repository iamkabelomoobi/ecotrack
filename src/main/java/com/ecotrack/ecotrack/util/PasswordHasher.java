package com.ecotrack.ecotrack.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class for generating hashed passwords.
 * This is for development purposes only and should not be used in production.
 */
public class PasswordHasher {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Example passwords
        System.out.println("Admin Password: " + encoder.encode("admin123"));
        System.out.println("Driver Password: " + encoder.encode("driver123"));
        System.out.println("Customer Password: " + encoder.encode("customer123"));
    }
}