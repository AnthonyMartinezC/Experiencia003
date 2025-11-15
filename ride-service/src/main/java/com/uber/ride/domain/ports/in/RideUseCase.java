package com.uber.ride.domain.ports.in;

import com.uber.ride.domain.model.Ride;
import java.util.List;
import java.util.Optional;

/**
 * 🚪 PUERTO DE ENTRADA - Ride Use Cases
 *
 * Define las operaciones que se pueden hacer con viajes.
 */
public interface RideUseCase {

    // Solicitar un nuevo viaje
    Ride requestRide(Long passengerId, String pickup, String dropoff);

    // Asignar conductor a un viaje
    Ride assignDriver(Long rideId, Long driverId);

    // Iniciar un viaje
    Ride startRide(Long rideId);

    // Completar un viaje
    Ride completeRide(Long rideId, double fare);

    // Cancelar un viaje
    Ride cancelRide(Long rideId);

    // Obtener un viaje por ID
    Optional<Ride> getRideById(Long id);

    // Obtener todos los viajes
    List<Ride> getAllRides();

    // Obtener viajes de un pasajero
    List<Ride> getRidesByPassenger(Long passengerId);

    // Obtener viajes de un conductor
    List<Ride> getRidesByDriver(Long driverId);
}
