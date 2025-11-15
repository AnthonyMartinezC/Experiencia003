package com.uber.ride.infrastructure.adapters.out.persistence;

import com.uber.ride.domain.model.Ride;
import com.uber.ride.domain.ports.out.RideRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 🔌 ADAPTADOR DE PERSISTENCIA - Ride Repository Adapter
 */
@Component
public class RideRepositoryAdapter implements RideRepository {

    private final JpaRideRepository jpaRideRepository;

    public RideRepositoryAdapter(JpaRideRepository jpaRideRepository) {
        this.jpaRideRepository = jpaRideRepository;
    }

    @Override
    public Ride save(Ride ride) {
        RideEntity entity = toEntity(ride);
        RideEntity savedEntity = jpaRideRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Ride> findById(Long id) {
        return jpaRideRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Ride> findAll() {
        return jpaRideRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ride> findByPassengerId(Long passengerId) {
        return jpaRideRepository.findByPassengerId(passengerId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ride> findByDriverId(Long driverId) {
        return jpaRideRepository.findByDriverId(driverId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRideRepository.existsById(id);
    }

    // ===== CONVERSIÓN =====

    private RideEntity toEntity(Ride ride) {
        RideEntity.RideStatus entityStatus = ride.getStatus() != null
                ? RideEntity.RideStatus.valueOf(ride.getStatus().name())
                : RideEntity.RideStatus.REQUESTED;

        return new RideEntity(
                ride.getId(),
                ride.getPassengerId(),
                ride.getDriverId(),
                ride.getPickupLocation(),
                ride.getDropoffLocation(),
                entityStatus,
                ride.getFare(),
                ride.getRequestedAt(),
                ride.getStartedAt(),
                ride.getCompletedAt()
        );
    }

    private Ride toDomain(RideEntity entity) {
        Ride ride = new Ride(
                entity.getPassengerId(),
                entity.getPickupLocation(),
                entity.getDropoffLocation()
        );

        ride.setId(entity.getId());
        ride.setDriverId(entity.getDriverId());
        ride.setStatus(Ride.RideStatus.valueOf(entity.getStatus().name()));
        ride.setFare(entity.getFare());
        ride.setRequestedAt(entity.getRequestedAt());
        ride.setStartedAt(entity.getStartedAt());
        ride.setCompletedAt(entity.getCompletedAt());

        return ride;
    }
}
