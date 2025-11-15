# 🏛️ Arquitectura Hexagonal - Explicación Visual

## 📐 Diagrama de Arquitectura Hexagonal

```
                    ARQUITECTURA HEXAGONAL

┌─────────────────────────────────────────────────────────────┐
│                                                               │
│                    MUNDO EXTERIOR                             │
│                                                               │
│  ┌──────────────┐              ┌──────────────┐              │
│  │  HTTP/REST   │              │   DATABASE   │              │
│  │              │              │     (H2)     │              │
│  │   Cliente    │              │              │              │
│  │   (Postman)  │              │              │              │
│  └───────┬──────┘              └──────┬───────┘              │
│          │                            │                       │
│          │ JSON                       │ SQL                   │
│          ▼                            ▼                       │
│  ┌───────────────┐           ┌────────────────┐              │
│  │  ADAPTADOR    │           │   ADAPTADOR    │              │
│  │   DE ENTRADA  │           │   DE SALIDA    │              │
│  │               │           │                │              │
│  │ UserController│           │UserRepository  │              │
│  │   @RestCtrl   │           │   Adapter      │              │
│  └───────┬───────┘           └────────┬───────┘              │
│          │                            │                       │
│          │ Traduce                    │ Traduce               │
│          │ HTTP → Dominio             │ Dominio → JPA         │
│          │                            │                       │
└──────────┼────────────────────────────┼───────────────────────┘
           │                            │
           │                            │
    ┌──────▼────────────────────────────▼────────┐
    │                                             │
    │          PUERTOS (Interfaces)               │
    │                                             │
    │    ┌──────────────────────────────┐        │
    │    │   UserUseCase (Input Port)   │        │
    │    │   - registerUser()            │        │
    │    │   - getUserById()             │        │
    │    │   - getAvailableDrivers()     │        │
    │    └──────────────┬────────────────┘        │
    │                   │                         │
    │    ┌──────────────▼─────────────────┐      │
    │    │       CAPA DE APLICACIÓN        │      │
    │    │                                 │      │
    │    │        UserService              │      │
    │    │     (Lógica de Negocio)         │      │
    │    │                                 │      │
    │    │   - Coordina operaciones        │      │
    │    │   - Valida reglas de negocio    │      │
    │    └──────────────┬─────────────────┘      │
    │                   │                         │
    │    ┌──────────────▼──────────────────┐     │
    │    │    UserRepository (Output Port)  │     │
    │    │    - save()                      │     │
    │    │    - findById()                  │     │
    │    │    - findActiveDrivers()         │     │
    │    └──────────────────────────────────┘     │
    │                                             │
    └─────────────────────────────────────────────┘
                        │
                        │
          ┌─────────────▼─────────────┐
          │                           │
          │    CAPA DE DOMINIO        │
          │      (CORAZÓN)            │
          │                           │
          │  ┌─────────────────────┐  │
          │  │      User.java      │  │
          │  │                     │  │
          │  │  - id               │  │
          │  │  - name             │  │
          │  │  - userType         │  │
          │  │  - rating           │  │
          │  │                     │  │
          │  │  canDrive() {       │  │
          │  │    rating >= 3.0    │  │
          │  │  }                  │  │
          │  └─────────────────────┘  │
          │                           │
          │  ✅ Java PURO             │
          │  ✅ Sin dependencias      │
          │  ✅ Lógica de negocio     │
          │                           │
          └───────────────────────────┘
```

---

## 🔄 Flujo de una petición HTTP

```
1️⃣ Cliente hace petición
   ↓
   POST /api/users
   {
     "name": "Juan",
     "email": "juan@email.com",
     "userType": "DRIVER"
   }

2️⃣ UserController (Adaptador de Entrada)
   ↓
   @PostMapping
   public ResponseEntity<User> registerUser(@RequestBody User user) {
       User registered = userUseCase.registerUser(user);
       return ResponseEntity.ok(registered);
   }

3️⃣ UserService (Aplicación)
   ↓
   public User registerUser(User user) {
       // Validaciones de negocio
       if (user.getName() == null) throw new Exception();

       // Llama al repositorio
       return userRepository.save(user);
   }

4️⃣ UserRepositoryAdapter (Adaptador de Salida)
   ↓
   public User save(User user) {
       // Convierte User (dominio) → UserEntity (JPA)
       UserEntity entity = toEntity(user);

       // Guarda en base de datos
       UserEntity saved = jpaRepository.save(entity);

       // Convierte UserEntity → User (dominio)
       return toDomain(saved);
   }

5️⃣ Base de Datos
   ↓
   INSERT INTO users (name, email, user_type, rating)
   VALUES ('Juan', 'juan@email.com', 'DRIVER', 5.0);

6️⃣ Respuesta al Cliente
   ↓
   {
     "id": 1,
     "name": "Juan",
     "email": "juan@email.com",
     "userType": "DRIVER",
     "rating": 5.0
   }
```

---

## 🎯 Separación de responsabilidades

### ¿Por qué User y UserEntity son diferentes?

```
┌─────────────────────────────────────────────────────────────┐
│                    User.java (DOMINIO)                       │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  public class User {                                         │
│      private Long id;                                        │
│      private String name;                                    │
│      private UserType userType;                              │
│      private double rating;                                  │
│                                                               │
│      // ✅ Lógica de negocio                                 │
│      public boolean canDrive() {                             │
│          return userType == DRIVER && rating >= 3.0;         │
│      }                                                        │
│  }                                                            │
│                                                               │
│  ✅ Java puro                                                 │
│  ✅ Sin anotaciones de frameworks                            │
│  ✅ Puede cambiar de DB sin modificarse                      │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                UserEntity.java (INFRAESTRUCTURA)             │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  @Entity                                                     │
│  @Table(name = "users")                                      │
│  public class UserEntity {                                   │
│      @Id                                                     │
│      @GeneratedValue(strategy = GenerationType.IDENTITY)     │
│      private Long id;                                        │
│                                                               │
│      @Column(nullable = false)                               │
│      private String name;                                    │
│                                                               │
│      @Enumerated(EnumType.STRING)                            │
│      private UserType userType;                              │
│                                                               │
│      @Column(nullable = false)                               │
│      private double rating;                                  │
│  }                                                            │
│                                                               │
│  ❌ Depende de JPA                                           │
│  ❌ Anotaciones específicas de base de datos                 │
│  ❌ Si cambias de DB, esto se modifica                       │
└─────────────────────────────────────────────────────────────┘
```

### Conversión entre User y UserEntity

```java
// Adaptador traduce entre ambos mundos

private UserEntity toEntity(User user) {
    return new UserEntity(
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getUserType()
    );
}

private User toDomain(UserEntity entity) {
    return new User(
        entity.getId(),
        entity.getName(),
        entity.getEmail(),
        entity.getUserType()
    );
}
```

---

## 🔌 Puertos y Adaptadores explicados

```
                    PUERTOS (Interfaces)
                         │
        ┌────────────────┼────────────────┐
        │                │                │
        ▼                ▼                ▼

INPUT PORT         DOMINIO         OUTPUT PORT
(Entrada)                          (Salida)

UserUseCase                     UserRepository
    │                               │
    │ ¿Qué puedo                    │ ¿Cómo guardo
    │ hacer?                        │ los datos?
    │                               │
    ▼                               ▼

registerUser()                  save()
getUserById()                   findById()
getAvailableDrivers()           findAll()
```

### Adaptadores conectan Puertos con Tecnologías

```
ADAPTADORES DE ENTRADA          ADAPTADORES DE SALIDA
(Reciben del exterior)          (Envían al exterior)

┌──────────────────┐            ┌──────────────────┐
│  UserController  │            │ UserRepository   │
│   (REST/HTTP)    │            │    Adapter       │
│                  │            │   (JPA/H2)       │
│  @PostMapping    │            │                  │
│  @GetMapping     │            │  save()          │
│  @PutMapping     │            │  findById()      │
└──────────────────┘            └──────────────────┘

Podrías cambiar a:              Podrías cambiar a:
- GraphQL                       - MongoDB
- gRPC                          - PostgreSQL
- WebSockets                    - Redis
- CLI                           - Cassandra

SIN TOCAR EL DOMINIO ✅
```

---

## 🏗️ Estructura de carpetas explicada

```
user-service/
│
├── domain/                          🎯 CAPA DE DOMINIO
│   │
│   ├── model/                       📦 Entidades de negocio
│   │   └── User.java               ← Java puro, sin dependencias
│   │
│   ├── ports/in/                    🚪 PUERTOS DE ENTRADA
│   │   └── UserUseCase.java        ← Interface: ¿Qué puedo hacer?
│   │
│   └── ports/out/                   🚪 PUERTOS DE SALIDA
│       └── UserRepository.java     ← Interface: ¿Cómo guardo?
│
├── application/                     🧠 CAPA DE APLICACIÓN
│   └── services/
│       └── UserService.java        ← Implementa UserUseCase
│                                      Coordina lógica de negocio
│
└── infrastructure/                  🔧 CAPA DE INFRAESTRUCTURA
    │
    ├── adapters/in/rest/            📡 ADAPTADORES DE ENTRADA
    │   └── UserController.java     ← Recibe HTTP, usa UserUseCase
    │
    └── adapters/out/persistence/    💾 ADAPTADORES DE SALIDA
        ├── UserEntity.java         ← Entidad JPA (con @Entity)
        ├── JpaUserRepository.java  ← Interface de Spring Data
        └── UserRepositoryAdapter   ← Implementa UserRepository
                                       usando JPA
```

---

## 🌐 Vista general de los 4 Microservicios

```
                   APLICACIÓN TIPO UBER
                          │
    ┌─────────────────────┼─────────────────────┬──────────────┐
    │                     │                     │              │
    ▼                     ▼                     ▼              ▼

USER SERVICE         RIDE SERVICE        LOCATION          PAYMENT
  :8081                :8082             SERVICE           SERVICE
                                         :8083             :8084

┌──────────┐         ┌──────────┐      ┌──────────┐     ┌──────────┐
│ User     │         │ Ride     │      │ Location │     │ Payment  │
│          │         │          │      │          │     │          │
│ - id     │         │ - id     │      │ -driverId│     │ - rideId │
│ - name   │         │ - pass   │      │ - lat    │     │ - amount │
│ - type   │◄────────│ - driver │      │ - lon    │     │ - status │
│ - rating │         │ - status │      │ - time   │     │ - method │
└──────────┘         └──────────┘      └──────────┘     └──────────┘
                           │
                           │
                           ▼
                   Estados del Viaje:
                   REQUESTED →
                   ACCEPTED →
                   IN_PROGRESS →
                   COMPLETED
```

### Comunicación entre Microservicios

```
1. Pasajero solicita viaje
   ↓
   RIDE SERVICE
   │
   ├→ Consulta USER SERVICE: ¿Hay conductores disponibles?
   │
   ├→ Consulta LOCATION SERVICE: ¿Dónde están los conductores?
   │
   └→ Al finalizar, llama a PAYMENT SERVICE: Procesar pago

Cada servicio es INDEPENDIENTE pero se COMUNICAN vía HTTP/REST
```

---

## 💡 Ventajas de esta arquitectura

### 1. Testeable

```java
// Puedes testear el dominio SIN base de datos ni HTTP

@Test
public void testCanDrive() {
    User driver = new User(1L, "Juan", "juan@email.com", "123", DRIVER);
    driver.setRating(4.5);

    assertTrue(driver.canDrive());  // ✅ Test puro, sin dependencias
}
```

### 2. Cambiable

```
┌─────────────────────────────────────────────────┐
│  Cambiar REST por GraphQL                       │
│  Solo modificas UserController                  │
│  El dominio NO cambia ✅                        │
└─────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────┐
│  Cambiar H2 por PostgreSQL                      │
│  Solo modificas UserRepositoryAdapter           │
│  El dominio NO cambia ✅                        │
└─────────────────────────────────────────────────┘
```

### 3. Clara separación de responsabilidades

```
DOMINIO          → ¿QUÉ hace la aplicación? (Reglas de negocio)
APLICACIÓN       → ¿CÓMO coordina las operaciones?
INFRAESTRUCTURA  → ¿CON QUÉ tecnologías trabaja?
```

---

## 🎓 Resumen ejecutivo

**Arquitectura Hexagonal** = Mantener la lógica de negocio independiente

**Microservicios** = Dividir la aplicación en servicios pequeños e independientes

**Resultado** = Código limpio, testeable, escalable y mantenible

---

**¡Aprende viendo el código!** Cada archivo tiene comentarios explicando qué hace.
