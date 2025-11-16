#  Microservicios con Spring Boot - App tipo Uber

>  Aprende cómo está implementada una arquitectura de microservicios en Java/Spring Boot de forma MUY sencilla.

---
> Learn By Doing.

--- 

## ¿Qué encontrarás aquí?

Este proyecto muestra **CÓMO** se implementa una arquitectura de microservicios usando **Spring Boot** y **Arquitectura Hexagonal**, con un ejemplo práctico: una app tipo Uber.

**NO** explicaré la teoría de qué son los microservicios (eso está en mil lugares), sino **CÓMO está construido el código** para que lo entiendas de forma práctica.

---

## Explicación para principiantes (13 años)

### ¿Qué problema resolvemos?

Imagina que quieres hacer una app como Uber. Si haces TODO en un solo programa gigante:
- Si una parte falla, **toda la app se cae**
- Es difícil de entender y modificar
- Muchos programadores trabajando en el mismo archivo = **caos**

### La solución: Microservicios

En lugar de 1 programa gigante, hacemos **4 programas pequeños** (microservicios) que trabajan juntos:

```
┌─────────────────────────────────────────────────────────┐
│                    APP TIPO UBER                         │
└─────────────────────────────────────────────────────────┘
                          │
        ┌─────────────────┼─────────────────┬──────────────┐
        │                 │                 │              │
   ┌────▼────┐      ┌─────▼──────┐    ┌────▼────┐   ┌─────▼──────┐
   │  USER   │      │   RIDE     │    │LOCATION │   │  PAYMENT   │
   │ SERVICE │      │  SERVICE   │    │ SERVICE │   │  SERVICE   │
   │  :8081  │      │   :8082    │    │  :8083  │   │   :8084    │
   └─────────┘      └────────────┘    └─────────┘   └────────────┘
   Usuarios         Viajes            GPS           Pagos
   (Pasajeros/      (Carreras)        (Ubicaciones) (Dinero)
   Conductores)
```

**Cada servicio**:
- ✅ Funciona de forma independiente
- ✅ Tiene su propia base de datos
- ✅ Se puede modificar sin romper los demás
- ✅ Un equipo diferente puede trabajar en cada uno

---

## Arquitectura Hexagonal (Puertos y Adaptadores)

Cada microservicio está organizado en **3 capas** como una cebolla:

```
┌──────────────────────────────────────────────────────────┐
│                   INFRAESTRUCTURA                        │
│  (REST Controllers, Base de Datos JPA, Configuración)    │
│  ┌──────────────────────────────────────────────────┐   │
│  │            APLICACIÓN (Services)                  │   │
│  │  ┌──────────────────────────────────────────┐    │   │
│  │  │      DOMINIO (Lógica de Negocio)         │    │   │
│  │  │  User, Ride, Location, Payment           │    │   │
│  │  │  ↑ PURO - No depende de nada            │    │   │
│  │  └──────────────────────────────────────────┘    │   │
│  │  ↑ Usa el dominio                                │   │
│  └──────────────────────────────────────────────────┘   │
│  ↑ Recibe HTTP y guarda en DB                           │
└──────────────────────────────────────────────────────────┘
```

### ¿Por qué 3 capas?

1. **DOMINIO** (Centro): La lógica de negocio pura
   - Ejemplo: "Un conductor debe tener rating > 3.0 para conducir"
   - **NO** sabe de HTTP, bases de datos ni frameworks

2. **APLICACIÓN** (Medio): Coordina las operaciones
   - Conecta el dominio con el exterior
   - Implementa los "casos de uso"

3. **INFRAESTRUCTURA** (Exterior): Tecnología específica
   - REST Controllers (reciben peticiones HTTP)
   - Repositorios JPA (guardan en base de datos)
   - Configuración de Spring Boot

---

## Estructura del Proyecto

```
Experiencia003/
│
├── user-service/              # 👤 Microservicio de Usuarios
│   ├── domain/
│   │   ├── model/            # User.java (Pasajero/Conductor)
│   │   ├── ports/in/         # UserUseCase.java (Qué puede hacer)
│   │   └── ports/out/        # UserRepository.java (Cómo guardar)
│   ├── application/
│   │   └── services/         # UserService.java (Lógica)
│   └── infrastructure/
│       ├── adapters/in/rest/ # UserController.java (HTTP)
│       └── adapters/out/     # JPA (Base de datos)
│
├── ride-service/              # 🚗 Microservicio de Viajes
│   ├── domain/
│   │   └── model/            # Ride.java (Estados: REQUESTED → ACCEPTED → IN_PROGRESS → COMPLETED)
│   ├── application/
│   │   └── services/         # RideService.java
│   └── infrastructure/
│       └── adapters/         # REST + JPA
│
├── location-service/          # 📍 Microservicio de Ubicaciones GPS
│   └── domain/model/         # Location.java (lat, lon, timestamp)
│
└── payment-service/           # 💳 Microservicio de Pagos
    └── domain/model/         # Payment.java (tarjeta, efectivo, wallet)
```

---

## Cómo funciona - Explicación paso a paso

### Ejemplo: Un pasajero solicita un viaje

```
1️⃣ PASAJERO pide viaje
   ↓
   POST http://localhost:8082/api/rides/request
   {
     "passengerId": 1,
     "pickup": "Calle 123",
     "dropoff": "Avenida 456"
   }

2️⃣ RIDE SERVICE crea el viaje
   → Estado: REQUESTED
   → Guarda en base de datos

3️⃣ SE BUSCA CONDUCTOR disponible
   GET http://localhost:8081/api/users/drivers/available
   → USER SERVICE devuelve conductores activos

4️⃣ SE ASIGNA CONDUCTOR
   PUT http://localhost:8082/api/rides/1/assign-driver?driverId=5
   → Estado cambia a: ACCEPTED

5️⃣ CONDUCTOR INICIA VIAJE
   PUT http://localhost:8082/api/rides/1/start
   → Estado cambia a: IN_PROGRESS

6️⃣ CONDUCTOR COMPLETA VIAJE
   PUT http://localhost:8082/api/rides/1/complete?fare=25.50
   → Estado cambia a: COMPLETED

7️⃣ SE PROCESA EL PAGO
   POST http://localhost:8084/api/payments
   {
     "rideId": 1,
     "amount": 25.50,
     "method": "CREDIT_CARD"
   }
   → PAYMENT SERVICE cobra al pasajero
```

---

## Código explicado - User Service

Veamos cómo está implementado el User Service:

### 1. **Dominio** - `User.java`

```java
public class User {
    private Long id;
    private String name;
    private UserType userType;  // PASSENGER o DRIVER
    private double rating;

    // LÓGICA DE NEGOCIO (sin dependencias externas)
    public boolean canDrive() {
        return this.userType == UserType.DRIVER
               && this.rating >= 3.0;
    }
}
```

**¿Por qué está separado?**
- Es Java puro, sin anotaciones de Spring o JPA
- La lógica de negocio NO depende de frameworks
- Puedes cambiar de Spring a otro framework sin tocar esto

### 2. **Puerto de Entrada** - `UserUseCase.java`

```java
public interface UserUseCase {
    User registerUser(User user);
    List<User> getAvailableDrivers();
    User updateUserRating(Long userId, double rating);
}
```

**¿Qué es esto?**
- Un "contrato" que dice qué operaciones ofrece el servicio
- Como un menú de restaurante: lista lo que puedes pedir

### 3. **Servicio** - `UserService.java`

```java
@Service
public class UserService implements UserUseCase {
    private final UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        System.out.println("👤 Registrando usuario: " + user.getName());
        return userRepository.save(user);
    }
}
```

**¿Qué hace?**
- Implementa la lógica de negocio
- Usa el repositorio para guardar datos
- Es el "cerebro" que coordina todo

### 4. **Controlador REST** - `UserController.java`

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserUseCase userUseCase;

    @PostMapping
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registered = userUseCase.registerUser(user);
        return ResponseEntity.ok(registered);
    }
}
```

**¿Qué hace?**
- Recibe peticiones HTTP (POST, GET, PUT, DELETE)
- Las traduce para que el dominio las entienda
- Es el "recepcionista" de tu app

### 5. **Adaptador de Base de Datos** - `UserRepositoryAdapter.java`

```java
@Component
public class UserRepositoryAdapter implements UserRepository {
    private final JpaUserRepository jpaRepository;

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);  // User → UserEntity
        UserEntity saved = jpaRepository.save(entity);
        return toDomain(saved);  // UserEntity → User
    }
}
```

**¿Por qué dos clases: User y UserEntity?**
- `User` = Dominio (no sabe de bases de datos)
- `UserEntity` = JPA (tiene anotaciones @Entity, @Table, etc.)
- El adaptador **traduce** entre ambos mundos

---

## Empresas que usan Microservicios

### Grandes empresas

| Empresa | Uso | Beneficio |
|---------|-----|-----------|
| **Netflix** | 1000+ microservicios | Tolerancia a fallos. Si el servicio de recomendaciones falla, puedes seguir viendo películas |
| **Amazon** | Cada función es un microservicio | Escalan Black Friday solo incrementando ciertos servicios |
| **Uber** | User, Ride, Payment, Location, etc. | Miles de viajes simultáneos sin problemas |
| **Spotify** | Playlists, Recomendaciones, Pagos | Cada equipo trabaja independiente |
| **Airbnb** | Reservas, Pagos, Mensajería | Pueden actualizar el chat sin tocar las reservas |

### Pequeñas empresas / Startups

| Tipo | Ejemplo | Microservicios |
|------|---------|----------------|
| **E-commerce** | Tienda online | Product, Cart, Payment, Shipping |
| **App de delivery** | Rappi, Glovo | User, Restaurant, Order, Delivery |
| **Fintech** | App de banco | Account, Transaction, Card, Loan |
| **SaaS** | Herramienta de marketing | User, Campaign, Analytics, Billing |

---

## ⚙️ Tecnologías usadas

### Stack principal

-  **Java 17** - Lenguaje de programación
-  **Spring Boot 3.2** - Framework para crear apps
-  **Spring Data JPA** - Para guardar en base de datos
-  **H2 Database** - Base de datos en memoria (para aprender)
-  **Maven** - Gestiona dependencias

### Arquitectura

-  **Microservicios** - Servicios independientes
-  **Arquitectura Hexagonal** - Puertos y adaptadores
-  **REST APIs** - Comunicación entre servicios

---

##  Cómo ejecutar el proyecto

### Requisitos previos

- Java 17 o superior
- Maven 3.6+

### Ejecutar cada microservicio

```bash
# Terminal 1 - User Service
cd user-service
mvn spring-boot:run
# Se inicia en http://localhost:8081

# Terminal 2 - Ride Service
cd ride-service
mvn spring-boot:run
# Se inicia en http://localhost:8082

# Terminal 3 - Location Service (opcional)
cd location-service
mvn spring-boot:run
# Se inicia en http://localhost:8083

# Terminal 4 - Payment Service (opcional)
cd payment-service
mvn spring-boot:run
# Se inicia en http://localhost:8084
```

---

## Ejemplos de uso (con curl o Postman)

### 1. Registrar un pasajero

```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Pérez",
    "email": "juan@email.com",
    "phone": "+573001234567",
    "userType": "PASSENGER"
  }'
```

### 2. Registrar un conductor

```bash
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "María García",
    "email": "maria@email.com",
    "phone": "+573007654321",
    "userType": "DRIVER"
  }'
```

### 3. Solicitar un viaje

```bash
curl -X POST http://localhost:8082/api/rides/request \
  -H "Content-Type: application/json" \
  -d '{
    "passengerId": 1,
    "pickup": "Calle 100 #15-20",
    "dropoff": "Carrera 7 #45-30"
  }'
```

### 4. Ver conductores disponibles

```bash
curl http://localhost:8081/api/users/drivers/available
```

### 5. Asignar conductor al viaje

```bash
curl -X PUT "http://localhost:8082/api/rides/1/assign-driver?driverId=2"
```

### 6. Iniciar el viaje

```bash
curl -X PUT http://localhost:8082/api/rides/1/start
```

### 7. Completar el viaje

```bash
curl -X PUT "http://localhost:8082/api/rides/1/complete?fare=25.50"
```

---

##  Conceptos clave aprendidos

### Microservicios

✅ Cada servicio es independiente
✅ Tiene su propia base de datos
✅ Se comunican por HTTP (REST APIs)
✅ Pueden estar en diferentes servidores

### Arquitectura Hexagonal

✅ **Dominio** en el centro (lógica de negocio pura)
✅ **Puertos** definen contratos (interfaces)
✅ **Adaptadores** conectan con el exterior (REST, DB)
✅ Fácil de testear y cambiar tecnologías

### Spring Boot

✅ **@RestController** - Crear endpoints HTTP
✅ **@Service** - Lógica de negocio
✅ **@Repository** - Acceso a datos
✅ **@Entity** - Mapeo a base de datos
✅ **Inyección de dependencias** automática

---

##  Diagrama de flujo completo

```
┌─────────────┐
│  PASAJERO   │
│  (Cliente)  │
└──────┬──────┘
       │ 1. Solicitar viaje
       ▼
┌──────────────────────────────────────────┐
│         RIDE SERVICE (:8082)              │
│  → Crea viaje (estado: REQUESTED)        │
│  → Guarda en DB                           │
└──────┬───────────────────────────────────┘
       │ 2. Buscar conductor
       ▼
┌──────────────────────────────────────────┐
│         USER SERVICE (:8081)              │
│  → Consulta conductores disponibles      │
│  → Filtra por rating >= 3.0               │
│  → Devuelve lista                         │
└──────┬───────────────────────────────────┘
       │ 3. Asignar conductor
       ▼
┌──────────────────────────────────────────┐
│         RIDE SERVICE (:8082)              │
│  → Asigna conductor al viaje              │
│  → Cambia estado a ACCEPTED               │
└──────┬───────────────────────────────────┘
       │ 4. Conductor inicia viaje
       ▼
┌──────────────────────────────────────────┐
│         RIDE SERVICE (:8082)              │
│  → Cambia estado a IN_PROGRESS           │
└──────┬───────────────────────────────────┘
       │ 5. Conductor completa viaje
       ▼
┌──────────────────────────────────────────┐
│         RIDE SERVICE (:8082)              │
│  → Cambia estado a COMPLETED              │
│  → Calcula tarifa                         │
└──────┬───────────────────────────────────┘
       │ 6. Procesar pago
       ▼
┌──────────────────────────────────────────┐
│       PAYMENT SERVICE (:8084)             │
│  → Procesa pago                           │
│  → Cobra al pasajero                      │
│  → Paga al conductor                      │
└──────────────────────────────────────────┘
```

---

##  Ventajas vs Desventajas

### Ventajas

- **Escalabilidad**: Crece solo lo que necesitas (ejemplo: Black Friday solo escala el servicio de pagos)
- **Independencia**: Cada equipo trabaja en su servicio sin molestar a los demás
- **Tolerancia a fallos**: Si un servicio falla, los demás siguen funcionando
- **Tecnologías diferentes**: Un servicio en Java, otro en Python, otro en Node.js
- **Despliegues independientes**: Actualizar el servicio de pagos sin tocar el resto

### ❌ Desventajas

- **Complejidad**: Es más difícil de configurar al inicio
- **Comunicación**: Los servicios deben hablar entre ellos (más lento que un monolito)
- **Debugging**: Más difícil encontrar errores cuando están distribuidos
- **Infraestructura**: Necesitas más recursos (servidores, contenedores, etc.)

---

##  Para empresas pequeñas

### ¿Cuándo usar microservicios?

✅ **SÍ** usa microservicios si:
- Tienes equipos diferentes trabajando en funcionalidades distintas
- Necesitas escalar partes específicas de tu app
- Planeas crecer rápidamente

❌ **NO** uses microservicios si:
- Tienes un equipo pequeño (1-3 personas)
- Tu app es simple y no va a crecer mucho
- Estás empezando (es mejor empezar con un monolito y migrar después)

### Proyectos ideales para pequeñas empresas

1. **E-commerce**: Product, Cart, Checkout, Payment
2. **App de delivery**: Restaurant, Order, Delivery, User
3. **SaaS**: Auth, Billing, Analytics, Notifications
4. **Fintech**: Account, Transaction, Card, Fraud Detection

---

##  Para entender mejor

### Analogía: Restaurante

**Monolito** (Un solo programa):
```
Una sola persona:
- Toma pedidos
- Cocina
- Cobra
- Limpia

❌ Si se enferma, el restaurante cierra
```

**Microservicios** (Programas separados):
```
Equipo especializado:
- Mesero (User Service)
- Cocinero (Order Service)
- Cajero (Payment Service)
- Repartidor (Delivery Service)

✅ Si el cajero falta, los demás siguen trabajando
```

---

##  Próximos pasos

Para mejorar este proyecto:

1. **API Gateway**: Un punto de entrada único para todos los servicios
2. **Service Discovery**: Los servicios se encuentran automáticamente (Eureka)
3. **Circuit Breaker**: Si un servicio falla, no tumba a los demás (Resilience4j)
4. **Mensajería**: Comunicación asíncrona con RabbitMQ o Kafka
5. **Contenedores**: Empaquetar con Docker
6. **Orquestación**: Desplegar con Kubernetes
7. **Seguridad**: Autenticación con JWT y Spring Security
8. **Monitoreo**: Logs centralizados con ELK Stack

---

##  Recursos adicionales

- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Arquitectura Hexagonal explicada](https://alistair.cockburn.us/hexagonal-architecture/)
- [Microservices Patterns - Chris Richardson](https://microservices.io/)
- [Spring Cloud](https://spring.io/projects/spring-cloud) - Para microservicios avanzados



**¿Preguntas?** Revisa el código, cada archivo tiene comentarios explicando qué hace. ¡Aprende haciendo!
