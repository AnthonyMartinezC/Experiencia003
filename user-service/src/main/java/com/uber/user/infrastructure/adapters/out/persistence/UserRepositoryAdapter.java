package com.uber.user.infrastructure.adapters.out.persistence;

import com.uber.user.domain.model.User;
import com.uber.user.domain.ports.out.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 🔌 ADAPTADOR DE PERSISTENCIA (Salida)
 *
 * Este adaptador "traduce" entre:
 * - User (dominio) ↔️ UserEntity (base de datos)
 *
 * Implementa UserRepository usando JPA por debajo.
 */
@Component
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryAdapter(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity savedEntity = jpaUserRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByUserType(User.UserType userType) {
        UserEntity.UserType entityType = UserEntity.UserType.valueOf(userType.name());
        return jpaUserRepository.findByUserType(entityType)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findActiveDrivers() {
        return jpaUserRepository.findActiveDrivers()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return jpaUserRepository.existsById(id);
    }

    // ===== CONVERSIÓN DOMINIO ↔️ ENTIDAD =====

    private UserEntity toEntity(User user) {
        UserEntity.UserType entityType = user.getUserType() != null
            ? UserEntity.UserType.valueOf(user.getUserType().name())
            : UserEntity.UserType.PASSENGER;

        return new UserEntity(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                entityType,
                user.getRating(),
                user.isActive()
        );
    }

    private User toDomain(UserEntity entity) {
        User.UserType domainType = User.UserType.valueOf(entity.getUserType().name());

        User user = new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhone(),
                domainType
        );
        user.setRating(entity.getRating());
        user.setActive(entity.isActive());
        return user;
    }
}
