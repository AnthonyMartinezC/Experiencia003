package com.uber.location.domain.model;

import java.time.LocalDateTime;

/**
 * 📍 DOMINIO - Entidad Location
 *
 * Representa la ubicación GPS de un conductor en tiempo real.
 */
public class Location {
    private Long id;
    private Long driverId;
    private double latitude;
    private double longitude;
    private LocalDateTime timestamp;
    private boolean isAvailable;  // Si el conductor está disponible para viajes

    public Location() {
    }

    public Location(Long driverId, double latitude, double longitude) {
        this.driverId = driverId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = LocalDateTime.now();
        this.isAvailable = true;
    }

    // 📌 LÓGICA DE NEGOCIO: Calcular distancia (simplificado)
    public double calculateDistance(double targetLat, double targetLon) {
        // Fórmula simplificada (en el mundo real usarías Haversine)
        double latDiff = targetLat - this.latitude;
        double lonDiff = targetLon - this.longitude;
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff) * 111; // Aprox. km
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}
