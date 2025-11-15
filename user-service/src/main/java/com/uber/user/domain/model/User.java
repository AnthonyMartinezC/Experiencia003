package com.uber.user.domain.model;

/**
 * 🎯 DOMINIO - Entidad User
 *
 * Representa un usuario en nuestra app tipo Uber.
 * Puede ser PASAJERO o CONDUCTOR.
 *
 * Esta clase es el CORAZÓN del negocio, no depende de nada externo.
 */
public class User {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private UserType userType;  // PASSENGER o DRIVER
    private double rating;      // Calificación del usuario (0-5)
    private boolean isActive;

    public enum UserType {
        PASSENGER,  // Pasajero
        DRIVER      // Conductor
    }

    public User() {
    }

    public User(Long id, String name, String email, String phone, UserType userType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.userType = userType;
        this.rating = 5.0;  // Todos empiezan con 5 estrellas
        this.isActive = true;
    }

    // 📌 LÓGICA DE NEGOCIO: Verificar si el usuario puede conducir
    public boolean canDrive() {
        return this.userType == UserType.DRIVER && this.isActive && this.rating >= 3.0;
    }

    // 📌 LÓGICA DE NEGOCIO: Actualizar calificación
    public void updateRating(double newRating) {
        if (newRating >= 0 && newRating <= 5) {
            this.rating = (this.rating + newRating) / 2;  // Promedio simple
        }
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userType=" + userType +
                ", rating=" + rating +
                '}';
    }
}
