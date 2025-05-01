package com.ecotrack.ecotrack.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String street;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String city;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String stateProvince;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9\\-\\s]{3,10}$")
    @Column(nullable = false, length = 10)
    private String postalCode;

    @NotBlank
    @Size(min = 2, max = 2)
    @Column(nullable = false, length = 2)
    private String country;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", unique = true)
    private Customer customer;
}