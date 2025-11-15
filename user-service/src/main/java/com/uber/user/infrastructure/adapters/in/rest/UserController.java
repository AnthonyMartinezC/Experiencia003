package com.uber.user.infrastructure.adapters.in.rest;

import com.uber.user.domain.model.User;
import com.uber.user.domain.ports.in.UserUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 📡 ADAPTADOR REST (Entrada)
 *
 * Este es el "punto de entrada" HTTP.
 * Recibe peticiones web y las traduce para que el dominio las entienda.
 *
 * Piénsalo como el "recepcionista" de tu app.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserUseCase userUseCase;

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    /**
     * POST /api/users
     * Registrar un nuevo usuario (pasajero o conductor)
     */
    @PostMapping
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userUseCase.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    /**
     * GET /api/users/{id}
     * Obtener un usuario específico
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userUseCase.getUserById(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET /api/users
     * Obtener todos los usuarios
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userUseCase.getAllUsers());
    }

    /**
     * GET /api/users/drivers/available
     * Obtener conductores disponibles
     */
    @GetMapping("/drivers/available")
    public ResponseEntity<List<User>> getAvailableDrivers() {
        return ResponseEntity.ok(userUseCase.getAvailableDrivers());
    }

    /**
     * PUT /api/users/{id}/rating
     * Actualizar la calificación de un usuario
     */
    @PutMapping("/{id}/rating")
    public ResponseEntity<User> updateRating(@PathVariable Long id, @RequestParam double rating) {
        try {
            User updatedUser = userUseCase.updateUserRating(id, rating);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * PUT /api/users/{id}/toggle-status
     * Activar/Desactivar un usuario
     */
    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<User> toggleStatus(@PathVariable Long id) {
        try {
            User updatedUser = userUseCase.toggleUserStatus(id);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
