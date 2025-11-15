package com.uber.ride.infrastructure.adapters.in.rest;

import com.uber.ride.domain.model.Ride;
import com.uber.ride.domain.ports.in.RideUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 📡 ADAPTADOR REST - Ride Controller
 *
 * Expone endpoints HTTP para gestionar viajes.
 */
@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideUseCase rideUseCase;

    public RideController(RideUseCase rideUseCase) {
        this.rideUseCase = rideUseCase;
    }

    /**
     * POST /api/rides/request
     * Solicitar un nuevo viaje
     */
    @PostMapping("/request")
    public ResponseEntity<Ride> requestRide(@RequestBody Map<String, Object> request) {
        Long passengerId = Long.valueOf(request.get("passengerId").toString());
        String pickup = request.get("pickup").toString();
        String dropoff = request.get("dropoff").toString();

        Ride ride = rideUseCase.requestRide(passengerId, pickup, dropoff);
        return new ResponseEntity<>(ride, HttpStatus.CREATED);
    }

    /**
     * PUT /api/rides/{id}/assign-driver
     * Asignar conductor a un viaje
     */
    @PutMapping("/{id}/assign-driver")
    public ResponseEntity<Ride> assignDriver(@PathVariable Long id, @RequestParam Long driverId) {
        try {
            Ride ride = rideUseCase.assignDriver(id, driverId);
            return ResponseEntity.ok(ride);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * PUT /api/rides/{id}/start
     * Iniciar un viaje
     */
    @PutMapping("/{id}/start")
    public ResponseEntity<Ride> startRide(@PathVariable Long id) {
        try {
            Ride ride = rideUseCase.startRide(id);
            return ResponseEntity.ok(ride);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * PUT /api/rides/{id}/complete
     * Completar un viaje
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<Ride> completeRide(@PathVariable Long id, @RequestParam double fare) {
        try {
            Ride ride = rideUseCase.completeRide(id, fare);
            return ResponseEntity.ok(ride);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * PUT /api/rides/{id}/cancel
     * Cancelar un viaje
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Ride> cancelRide(@PathVariable Long id) {
        try {
            Ride ride = rideUseCase.cancelRide(id);
            return ResponseEntity.ok(ride);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET /api/rides/{id}
     * Obtener un viaje por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ride> getRideById(@PathVariable Long id) {
        return rideUseCase.getRideById(id)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET /api/rides
     * Obtener todos los viajes
     */
    @GetMapping
    public ResponseEntity<List<Ride>> getAllRides() {
        return ResponseEntity.ok(rideUseCase.getAllRides());
    }

    /**
     * GET /api/rides/passenger/{passengerId}
     * Obtener viajes de un pasajero
     */
    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<Ride>> getRidesByPassenger(@PathVariable Long passengerId) {
        return ResponseEntity.ok(rideUseCase.getRidesByPassenger(passengerId));
    }

    /**
     * GET /api/rides/driver/{driverId}
     * Obtener viajes de un conductor
     */
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<Ride>> getRidesByDriver(@PathVariable Long driverId) {
        return ResponseEntity.ok(rideUseCase.getRidesByDriver(driverId));
    }
}
