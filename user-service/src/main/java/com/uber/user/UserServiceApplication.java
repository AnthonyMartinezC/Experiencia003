package com.uber.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 🚀 APLICACIÓN PRINCIPAL
 *
 * Punto de entrada del microservicio User Service.
 * Spring Boot configura todo automáticamente.
 */
@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("🚀 USER SERVICE - INICIADO");
        System.out.println("=".repeat(60));
        System.out.println("📍 URL: http://localhost:8081");
        System.out.println("📡 Endpoints:");
        System.out.println("   POST   /api/users                      - Registrar usuario");
        System.out.println("   GET    /api/users                      - Listar usuarios");
        System.out.println("   GET    /api/users/{id}                 - Obtener usuario");
        System.out.println("   GET    /api/users/drivers/available    - Conductores disponibles");
        System.out.println("   PUT    /api/users/{id}/rating?rating=4 - Actualizar rating");
        System.out.println("   PUT    /api/users/{id}/toggle-status   - Activar/Desactivar");
        System.out.println("=".repeat(60) + "\n");
    }
}
