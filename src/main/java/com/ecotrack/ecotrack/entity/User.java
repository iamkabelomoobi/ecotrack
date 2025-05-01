package com.ecotrack.ecotrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    public enum Role {
        ADMIN, DRIVER, CUSTOMER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String password;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9\\s-]{10,}$")
    @Column(unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Admin admin;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Customer customer;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Driver driver;

    public void setAdmin(Admin admin) {
        if (admin == null) {
            if (this.admin != null) {
                this.admin.setUser(null);
            }
        } else {
            admin.setUser(this);
            this.role = Role.ADMIN;
        }
        this.admin = admin;
    }

    public void setCustomer(Customer customer) {
        if (customer == null) {
            if (this.customer != null) {
                this.customer.setUser(null);
            }
        } else {
            customer.setUser(this);
            this.role = Role.CUSTOMER;
        }
        this.customer = customer;
    }

    public void setDriver(Driver driver) {
        if (driver == null) {
            if (this.driver != null) {
                this.driver.setUser(null);
            }
        } else {
            driver.setUser(this);
            this.role = Role.DRIVER;
        }
        this.driver = driver;
    }
}