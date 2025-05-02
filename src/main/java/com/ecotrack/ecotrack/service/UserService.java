package com.ecotrack.ecotrack.service;

import com.ecotrack.ecotrack.dto.UserDTO;
import com.ecotrack.ecotrack.entity.User;
import com.ecotrack.ecotrack.exception.InvalidRequestException;
import com.ecotrack.ecotrack.mapper.UserMapper;
import com.ecotrack.ecotrack.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service class for managing user-related operations.
 */
@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /**
     * Constructor for UserService.
     *
     * @param userRepository  the user repository
     * @param passwordEncoder the password encoder
     * @param userMapper      the user mapper
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Validates the complexity of a password.
     *
     * @param password the password to validate
     * @throws InvalidRequestException if the password does not meet complexity
     *                                 requirements
     */
    private void validatePasswordComplexity(String password) {
        if (password.length() < 8) {
            throw new InvalidRequestException("Password must be at least 8 characters");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new InvalidRequestException("Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new InvalidRequestException("Password must contain at least one lowercase letter");
        }
        if (!password.matches(".*\\d.*")) {
            throw new InvalidRequestException("Password must contain at least one digit");
        }
    }

    /**
     * Finds a user by email.
     *
     * @param email the email to search for
     * @return an Optional containing the user if found
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Finds a user by phone number.
     *
     * @param phone the phone number to search for
     * @return an Optional containing the user if found
     */
    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all users
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by ID.
     *
     * @param id the ID of the user
     * @return an Optional containing the user if found
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Saves a user.
     *
     * @param user the user to save
     * @return the saved user
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user to delete
     */
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Creates a new user.
     *
     * @param userDTO the user data transfer object
     * @return the created user as a DTO
     * @throws IllegalArgumentException if the email or phone number already exists
     */
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        if (findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (findByPhone(userDTO.getPhone()).isPresent()) {
            throw new IllegalArgumentException("Phone number already exists");
        }
        User user = UserMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return UserMapper.toDTO(userRepository.save(user));
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user
     * @return the user as a DTO
     * @throws IllegalArgumentException if the user is not found
     */
    @Transactional
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return UserMapper.toDTO(user);
    }

    /**
     * Updates a user's details.
     *
     * @param id      the ID of the user to update
     * @param userDTO the user data transfer object containing updated details
     * @return the updated user as a DTO
     * @throws IllegalArgumentException if the email or phone number is already
     *                                  taken
     */
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (userDTO.getEmail() != null) {
            userRepository.findByEmail(userDTO.getEmail()).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(id)) {
                    throw new IllegalArgumentException("Email is already taken");
                }
            });
            user.setEmail(userDTO.getEmail());
        }

        if (userDTO.getPhone() != null) {
            userRepository.findByPhone(userDTO.getPhone()).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(id)) {
                    throw new IllegalArgumentException("Phone number is already taken");
                }
            });
            user.setPhone(userDTO.getPhone());
        }

        return UserMapper.toDTO(userRepository.save(user));
    }

    /**
     * Updates a user's contact details.
     *
     * @param userId the ID of the user
     * @param email  the new email
     * @param phone  the new phone number
     * @return the updated user as a DTO
     * @throws IllegalArgumentException if the email or phone number is already
     *                                  taken
     */
    @Transactional
    public void updateUserContactDetails(Long userId, String email, String phone) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (email != null) {
            log.info("Updating email for userId {}: {}", userId, email);
            userRepository.findByEmail(email).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(userId)) {
                    throw new IllegalArgumentException("Email is already taken");
                }
            });
            user.setEmail(email);
        }

        if (phone != null) {
            log.info("Updating phone for userId {}: {}", userId, phone);
            userRepository.findByPhone(phone).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(userId)) {
                    throw new IllegalArgumentException("Phone number is already taken");
                }
            });
            user.setPhone(phone);
        }

        log.info("Saving updated user details for userId {}", userId);
        userRepository.save(user);
    }

    /**
     * Updates a user's password.
     *
     * @param userId          the ID of the user
     * @param currentPassword the current password
     * @param newPassword     the new password
     * @throws IllegalArgumentException if the current password is incorrect or the
     *                                  new password is invalid
     */
    @Transactional
    public void updatePassword(Long userId, String currentPassword, String newPassword) {
        validatePasswordComplexity(newPassword);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as the current password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
