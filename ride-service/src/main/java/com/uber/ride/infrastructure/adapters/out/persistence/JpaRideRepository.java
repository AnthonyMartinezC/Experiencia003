package com.uber.ride.infrastructure.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 🗄️ REPOSITORIO JPA - Ride Repository
 */
@Repository
public interface JpaRideRepository extends JpaRepository<RideEntity, Long> {

    List<RideEntity> findByPassengerId(Long passengerId);

    List<RideEntity> findByDriverId(Long driverId);
}
