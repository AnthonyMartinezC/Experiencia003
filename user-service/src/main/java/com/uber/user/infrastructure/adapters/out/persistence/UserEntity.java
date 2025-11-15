package com.uber.user.infrastructure.adapters.out.persistence;

import jakarta.persistence.*;

/**
 * 💾 ENTIDAD JPA (Base de Datos)
 *
 * Esta clase representa cómo se guarda un User en la base de datos.
 * Usa anotaciones de JPA (@Entity, @Table, etc.)
 *
 * IMPORTANTE: Es diferente a la clase User del dominio.
 * El dominio NO debe saber de bases de datos.
 */
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    @Column(nullable = false)
    private double rating = 5.0;

    @Column(nullable = false)
    private boolean isActive = true;

    public enum UserType {
        PASSENGER, DRIVER
    }

    // Constructores
    public UserEntity() {
    }

    public UserEntity(Long id, String name, String email, String phone, UserType userType, double rating, boolean isActive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
        this.rating = rating;
        this.isActive = isActive;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
