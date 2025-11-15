package com.uber.user.application.services;

import com.uber.user.domain.model.User;
import com.uber.user.domain.ports.in.UserUseCase;
import com.uber.user.domain.ports.out.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 🧠 SERVICIO DE APLICACIÓN
 *
 * Aquí está la LÓGICA DE NEGOCIO.
 * Coordina entre los puertos de entrada y salida.
 * Es el "cerebro" que toma decisiones.
 */
@Service
public class UserService implements UserUseCase {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(User user) {
        System.out.println("👤 Registrando nuevo " + user.getUserType() + ": " + user.getName());
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        System.out.println("🔍 Buscando usuario con ID: " + id);
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAvailableDrivers() {
        System.out.println("🚗 Buscando conductores disponibles...");
        return userRepository.findActiveDrivers();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUserRating(Long userId, double rating) {
        System.out.println("⭐ Actualizando calificación del usuario " + userId + " a " + rating);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.updateRating(rating);
        return userRepository.save(user);
    }

    @Override
    public User toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setActive(!user.isActive());
        System.out.println("🔄 Usuario " + userId + " ahora está: " +
                          (user.isActive() ? "ACTIVO" : "INACTIVO"));

        return userRepository.save(user);
    }
}
