package com.uber.ride.application.services;

import com.uber.ride.domain.model.Ride;
import com.uber.ride.domain.ports.in.RideUseCase;
import com.uber.ride.domain.ports.out.RideRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 🧠 SERVICIO DE APLICACIÓN - Ride Service
 *
 * Coordina la lógica de negocio de los viajes.
 */
@Service
public class RideService implements RideUseCase {

    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    @Override
    public Ride requestRide(Long passengerId, String pickup, String dropoff) {
        System.out.println("🚗 Nuevo viaje solicitado por pasajero ID: " + passengerId);
        Ride ride = new Ride(passengerId, pickup, dropoff);
        return rideRepository.save(ride);
    }

    @Override
    public Ride assignDriver(Long rideId, Long driverId) {
        System.out.println("👨‍✈️ Asignando conductor " + driverId + " al viaje " + rideId);

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

        ride.assignDriver(driverId);
        return rideRepository.save(ride);
    }

    @Override
    public Ride startRide(Long rideId) {
        System.out.println("🏁 Iniciando viaje " + rideId);

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

        ride.startRide();
        return rideRepository.save(ride);
    }

    @Override
    public Ride completeRide(Long rideId, double fare) {
        System.out.println("✅ Completando viaje " + rideId + " con tarifa: $" + fare);

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

        ride.completeRide(fare);
        return rideRepository.save(ride);
    }

    @Override
    public Ride cancelRide(Long rideId) {
        System.out.println("❌ Cancelando viaje " + rideId);

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

        ride.cancelRide();
        return rideRepository.save(ride);
    }

    @Override
    public Optional<Ride> getRideById(Long id) {
        return rideRepository.findById(id);
    }

    @Override
    public List<Ride> getAllRides() {
        return rideRepository.findAll();
    }

    @Override
    public List<Ride> getRidesByPassenger(Long passengerId) {
        return rideRepository.findByPassengerId(passengerId);
    }

    @Override
    public List<Ride> getRidesByDriver(Long driverId) {
        return rideRepository.findByDriverId(driverId);
    }
}
