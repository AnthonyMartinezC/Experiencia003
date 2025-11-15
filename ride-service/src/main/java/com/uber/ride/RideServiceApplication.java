package com.uber.ride;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 🚀 APLICACIÓN PRINCIPAL - Ride Service
 */
@SpringBootApplication
public class RideServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RideServiceApplication.class, args);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("🚀 RIDE SERVICE - INICIADO");
        System.out.println("=".repeat(60));
        System.out.println("📍 URL: http://localhost:8082");
        System.out.println("📡 Endpoints:");
        System.out.println("   POST   /api/rides/request                    - Solicitar viaje");
        System.out.println("   PUT    /api/rides/{id}/assign-driver         - Asignar conductor");
        System.out.println("   PUT    /api/rides/{id}/start                 - Iniciar viaje");
        System.out.println("   PUT    /api/rides/{id}/complete?fare=25.50   - Completar viaje");
        System.out.println("   PUT    /api/rides/{id}/cancel                - Cancelar viaje");
        System.out.println("   GET    /api/rides/{id}                       - Obtener viaje");
        System.out.println("   GET    /api/rides                            - Listar viajes");
        System.out.println("   GET    /api/rides/passenger/{id}             - Viajes de pasajero");
        System.out.println("   GET    /api/rides/driver/{id}                - Viajes de conductor");
        System.out.println("=".repeat(60) + "\n");
    }
}
