package com.uber.user.domain.ports.out;

import com.uber.user.domain.model.User;
import java.util.List;
import java.util.Optional;

/**
 * 🚪 PUERTO DE SALIDA
 *
 * Define CÓMO el dominio se comunica con el exterior (base de datos).
 * La implementación real está en la capa de infraestructura.
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    List<User> findByUserType(User.UserType userType);

    List<User> findActiveDrivers();

    boolean existsById(Long id);
}
