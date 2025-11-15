package com.uber.ride.domain.ports.out;

import com.uber.ride.domain.model.Ride;
import java.util.List;
import java.util.Optional;

/**
 * 🚪 PUERTO DE SALIDA - Ride Repository
 */
public interface RideRepository {

    Ride save(Ride ride);

    Optional<Ride> findById(Long id);

    List<Ride> findAll();

    List<Ride> findByPassengerId(Long passengerId);

    List<Ride> findByDriverId(Long driverId);

    boolean existsById(Long id);
}
