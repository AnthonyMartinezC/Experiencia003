package com.uber.user.infrastructure.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 🗄️ REPOSITORIO JPA
 *
 * Spring Data JPA nos da métodos GRATIS:
 * - save()
 * - findById()
 * - findAll()
 * - etc.
 *
 * Solo declaramos métodos extras que necesitemos.
 */
@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    // Buscar usuarios por tipo (PASSENGER o DRIVER)
    List<UserEntity> findByUserType(UserEntity.UserType userType);

    // Buscar conductores activos con buena calificación
    @Query("SELECT u FROM UserEntity u WHERE u.userType = 'DRIVER' AND u.isActive = true AND u.rating >= 3.0")
    List<UserEntity> findActiveDrivers();
}
