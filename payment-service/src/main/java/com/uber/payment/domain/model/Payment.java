package com.uber.payment.domain.model;

import java.time.LocalDateTime;

/**
 * 💳 DOMINIO - Entidad Payment
 *
 * Representa un pago por un viaje.
 */
public class Payment {
    private Long id;
    private Long rideId;
    private Long passengerId;
    private double amount;
    private PaymentStatus status;
    private PaymentMethod method;
    private LocalDateTime processedAt;

    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED
    }

    public enum PaymentMethod {
        CREDIT_CARD, DEBIT_CARD, CASH, DIGITAL_WALLET
    }

    public Payment() {
    }

    public Payment(Long rideId, Long passengerId, double amount, PaymentMethod method) {
        this.rideId = rideId;
        this.passengerId = passengerId;
        this.amount = amount;
        this.method = method;
        this.status = PaymentStatus.PENDING;
    }

    // 📌 LÓGICA DE NEGOCIO: Procesar pago
    public void processPayment() {
        // Simulación: 90% de éxito
        if (Math.random() > 0.1) {
            this.status = PaymentStatus.COMPLETED;
            this.processedAt = LocalDateTime.now();
        } else {
            this.status = PaymentStatus.FAILED;
        }
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRideId() { return rideId; }
    public void setRideId(Long rideId) { this.rideId = rideId; }
    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public PaymentMethod getMethod() { return method; }
    public void setMethod(PaymentMethod method) { this.method = method; }
    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
}
