# Auth Service (Nefetech)

El Auth Service es el microservicio centralizado encargado de la autenticación, emisión de tokens JWT y control de acceso (RBAC) para el ecosistema Nefetech.

---

## Requisitos Previos

Para levantar este proyecto en tu entorno local necesitas:

* Java 17 o superior.
* Maven 3.8+.
* PostgreSQL o MySQL.
* MongoDB.
* Redis.

---

## Variables de Entorno

Asegúrate de configurar las siguientes variables de entorno antes de arrancar. Por razones de seguridad, solicita las credenciales reales y los secretos al administrador del sistema. Nunca subas secretos reales al repositorio.

```bash
# Base de Datos SQL (Central)
SPRING_DATASOURCE_URL=jdbc:postgresql://<DB_HOST>:<DB_PORT>/<DB_NAME>
SPRING_DATASOURCE_USERNAME=<DB_USER>
SPRING_DATASOURCE_PASSWORD=<DB_PASSWORD>

# Base de Datos NoSQL (Auditoría)
SPRING_DATA_MONGODB_URI=mongodb://<MONGO_HOST>:<MONGO_PORT>/<MONGO_DB>

# Redis (Caché)
SPRING_REDIS_HOST=<REDIS_HOST>
SPRING_REDIS_PORT=<REDIS_PORT>

# Seguridad JWT
JWT_SECRET=<JWT_SECRET>
JWT_EXPIRATION_MS=<EXPIRATION_TIME_MS>
```

---

## Cómo levantar el proyecto localmente

1. Clona el repositorio.
2. Levanta las bases de datos locales (via Docker u on-premise).
3. Ejecuta la aplicación:
   ```bash
   mvn spring-boot:run
   ```
4. El servicio estará disponible en http://localhost:8080.

---

## Endpoints Principales

1. **POST `/api/v1/auth/login`**
   - Público. Valida email y password y retorna un JWT y expiración.
2. **POST `/api/v1/auth/register-business`**
   - Público. Endpoint de Onboarding. Crea una empresa (`Tenant`) y al usuario Dueño simultáneamente.
3. **POST `/api/v1/auth/register-employee`**
   - Privado (Requiere `Authorization: Bearer <token_admin>`). Crea un empleado/sub-usuario y lo asocia automáticamente a la empresa de quien ejecuta la petición.

---

## Prueba Rápida (Login)

Ejemplo de flujo principal de autenticación:

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
-H "Content-Type: application/json" \
-d '{
    "email": "<USER_EMAIL>",
    "password": "<USER_PASSWORD>",
    "deviceType": "desktop"
}'
```

---

## Estructura del Código (Hexagonal)

Este proyecto sigue una Arquitectura Hexagonal:
* `src/main/java/com/nefetech/auth/domain`: Entidades y reglas de negocio puras (User, Excepciones).
* `src/main/java/com/nefetech/auth/application`: Casos de uso de la aplicación (`LoginUseCase`, Puertos).
* `src/main/java/com/nefetech/auth/infrastructure/controller`: Endpoints REST (`AuthController`).
* `src/main/java/com/nefetech/auth/infrastructure/persistence`: Entidades JPA, Documentos Mongo y Adaptadores de Repositorios.
* `src/main/java/com/nefetech/auth/infrastructure/security`: Generador de Tokens JWT y Config de Spring Security.
