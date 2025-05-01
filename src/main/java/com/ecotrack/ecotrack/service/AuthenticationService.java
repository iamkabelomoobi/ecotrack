package com.ecotrack.ecotrack.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecotrack.ecotrack.config.security.JwtTokenProvider;
import com.ecotrack.ecotrack.dto.AdminRegistrationDTO;
import com.ecotrack.ecotrack.dto.CustomerRegistrationDTO;
import com.ecotrack.ecotrack.dto.DriverRegistrationDTO;
import com.ecotrack.ecotrack.entity.Admin;
import com.ecotrack.ecotrack.entity.Customer;
import com.ecotrack.ecotrack.entity.Driver;
import com.ecotrack.ecotrack.entity.User;
import com.ecotrack.ecotrack.entity.User.Role;
import com.ecotrack.ecotrack.exception.DuplicateException;
import com.ecotrack.ecotrack.exception.InvalidCredentialsException;
import com.ecotrack.ecotrack.repository.UserRepository;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private User createUser(String email, String phone, String password, User.Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateException("Account already exists.");
        }
        User user = new User();
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return user;
    }

    @Transactional
    public String registerAdmin(AdminRegistrationDTO adminDTO) {
        User user = createUser(adminDTO.getEmail(), adminDTO.getPhone(), adminDTO.getPassword(), Role.ADMIN);

        Admin admin = new Admin();
        admin.setFirstName(adminDTO.getFirstName());
        admin.setLastName(adminDTO.getLastName());
        admin.setUser(user);
        user.setAdmin(admin);

        userRepository.save(user);
        return jwtTokenProvider.generateToken(user.getId(), user.getRole().name());
    }

    @Transactional
    public String registerCustomer(CustomerRegistrationDTO customerDTO) {
        User user = createUser(customerDTO.getEmail(), customerDTO.getPhone(), customerDTO.getPassword(), Role.CUSTOMER);

        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setUser(user);
        user.setCustomer(customer);

        userRepository.save(user);
        return jwtTokenProvider.generateToken(user.getId(), user.getRole().name());
    }

    @Transactional
    public String registerDriver(DriverRegistrationDTO driverDTO) {
        User user = createUser(driverDTO.getEmail(), driverDTO.getPhone(), driverDTO.getPassword(), Role.DRIVER);

        Driver driver = new Driver();
        driver.setFirstName(driverDTO.getFirstName());
        driver.setLastName(driverDTO.getLastName());
        driver.setLicenseNumber(driverDTO.getLicenseNumber());
        driver.setVehicleRegistration(driverDTO.getVehicleRegistration());
        driver.setVehicleType(driverDTO.getVehicleType());
        driver.setUser(user);
        user.setDriver(driver);

        userRepository.save(user);
        return jwtTokenProvider.generateToken(user.getId(), user.getRole().name());
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password."));

                System.out.println("Password matches: " + passwordEncoder.matches(password, user.getPassword()));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        return jwtTokenProvider.generateToken(user.getId(), user.getRole().name());
    }

    public String refreshToken(String oldToken) {
        if (!jwtTokenProvider.validateToken(oldToken)) {
            throw new InvalidCredentialsException("Invalid or expired token.");
        }
        Long userId = jwtTokenProvider.getUserIdFromToken(oldToken);
        String role = jwtTokenProvider.getRoleFromToken(oldToken);
        return jwtTokenProvider.generateToken(userId, role);
    }
}
