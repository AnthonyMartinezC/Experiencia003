package com.uber.user.domain.ports.in;

import com.uber.user.domain.model.User;
import java.util.List;
import java.util.Optional;

/**
 * 🚪 PUERTO DE ENTRADA
 *
 * Define QUÉ operaciones puede hacer nuestra aplicación.
 * Es como el "menú" que ofrece el microservicio.
 *
 * Los controladores REST usarán estas operaciones.
 */
public interface UserUseCase {

    // Registrar un nuevo usuario (pasajero o conductor)
    User registerUser(User user);

    // Obtener un usuario por ID
    Optional<User> getUserById(Long id);

    // Obtener todos los conductores disponibles
    List<User> getAvailableDrivers();

    // Obtener todos los usuarios
    List<User> getAllUsers();

    // Actualizar calificación de un usuario
    User updateUserRating(Long userId, double rating);

    // Activar/Desactivar un usuario
    User toggleUserStatus(Long userId);
}
