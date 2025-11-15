package com.uber.ride.domain.model;

import java.time.LocalDateTime;

/**
 * 🚗 DOMINIO - Entidad Ride (Viaje)
 *
 * Representa un viaje/carrera en nuestra app tipo Uber.
 * Conecta un pasajero con un conductor.
 */
public class Ride {
    private Long id;
    private Long passengerId;       // ID del pasajero
    private Long driverId;          // ID del conductor
    private String pickupLocation;  // Ubicación de recogida
    private String dropoffLocation; // Ubicación de destino
    private RideStatus status;      // Estado del viaje
    private double fare;            // Tarifa del viaje
    private LocalDateTime requestedAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    public enum RideStatus {
        REQUESTED,   // Solicitado
        ACCEPTED,    // Aceptado por conductor
        IN_PROGRESS, // En curso
        COMPLETED,   // Completado
        CANCELLED    // Cancelado
    }

    public Ride() {
    }

    public Ride(Long passengerId, String pickupLocation, String dropoffLocation) {
        this.passengerId = passengerId;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.status = RideStatus.REQUESTED;
        this.requestedAt = LocalDateTime.now();
    }

    // 📌 LÓGICA DE NEGOCIO: Asignar conductor
    public void assignDriver(Long driverId) {
        if (this.status != RideStatus.REQUESTED) {
            throw new IllegalStateException("Solo se puede asignar conductor a viajes solicitados");
        }
        this.driverId = driverId;
        this.status = RideStatus.ACCEPTED;
    }

    // 📌 LÓGICA DE NEGOCIO: Iniciar viaje
    public void startRide() {
        if (this.status != RideStatus.ACCEPTED) {
            throw new IllegalStateException("El viaje debe estar aceptado para iniciarse");
        }
        this.status = RideStatus.IN_PROGRESS;
        this.startedAt = LocalDateTime.now();
    }

    // 📌 LÓGICA DE NEGOCIO: Completar viaje
    public void completeRide(double fare) {
        if (this.status != RideStatus.IN_PROGRESS) {
            throw new IllegalStateException("El viaje debe estar en progreso para completarse");
        }
        this.status = RideStatus.COMPLETED;
        this.fare = fare;
        this.completedAt = LocalDateTime.now();
    }

    // 📌 LÓGICA DE NEGOCIO: Cancelar viaje
    public void cancelRide() {
        if (this.status == RideStatus.COMPLETED) {
            throw new IllegalStateException("No se puede cancelar un viaje completado");
        }
        this.status = RideStatus.CANCELLED;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropoffLocation() { return dropoffLocation; }
    public void setDropoffLocation(String dropoffLocation) { this.dropoffLocation = dropoffLocation; }

    public RideStatus getStatus() { return status; }
    public void setStatus(RideStatus status) { this.status = status; }

    public double getFare() { return fare; }
    public void setFare(double fare) { this.fare = fare; }

    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }

    @Override
    public String toString() {
        return "Ride{" +
                "id=" + id +
                ", passengerId=" + passengerId +
                ", driverId=" + driverId +
                ", status=" + status +
                ", fare=" + fare +
                '}';
    }
}
