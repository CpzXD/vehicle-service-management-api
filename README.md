# Vehicle Service Management API

A robust RESTful API built with **Java 21**, **Spring Boot 3**, and **Maven** for managing vehicle records in an automotive service environment. 

Features comprehensive input validation, business logic enforcement (preventing duplicate license plates and preventing illegal mileage decreases), centralized error handling, and end-to-end integration and unit tests.

---

## Key Features

- **RESTful Endpoints**: Full CRUD operations for vehicle records (`POST`, `GET`, `PUT`, `DELETE`).
- **Domain Business Rules**:
  - Uniqueness constraint enforcement for license plates (`DuplicateLicensePlateException`).
  - Validation ensuring recorded vehicle mileage does not decrease between service updates (`MileageDecreaseException`).
- **Centralized Error Handling**: Standardized error responses using `@ControllerAdvice` (`GlobalExceptionHandler`).
- **Input Validation**: Strict request payload validation using Jakarta Bean Validation (`@Valid`, `@NotBlank`, `@Min`).
- **Automated Testing Suite**: Comprehensive unit tests (`VehicleServiceTest`) and integration tests (`VehicleApiTest`, `MockMvc`).

---

## Tech Stack

- **Java**: Version 21
- **Framework**: Spring Boot 3
- **Build Tool**: Apache Maven (includes Maven Wrapper `mvnw`)
- **Testing**: JUnit 5, AssertJ, Spring Boot Test / MockMvc

---

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/vehicles` | List all vehicles (supports filtering by `?brand=...`) |
| `GET` | `/api/vehicles/{id}` | Get vehicle details by ID |
| `POST` | `/api/vehicles` | Register a new vehicle |
| `PUT` | `/api/vehicles/{id}` | Update existing vehicle attributes and mileage |
| `DELETE` | `/api/vehicles/{id}` | Remove a vehicle record |

### Example Request Body (POST / PUT)

```json
{
  "brand": "Volkswagen",
  "model": "Golf",
  "licensePlate": "B-123-XYZ",
  "mileage": 145000
}
```

---

## Build and Run

### Prerequisites
- JDK 21+ installed

### Running with Maven Wrapper

```bash
# Clone the repository
git clone https://github.com/CpzXD/vehicle-service-management-api.git
cd vehicle-service-management-api

# Run automated tests
./mvnw test

# Start the application locally on http://localhost:8080
./mvnw spring-boot:run
```

On Windows Command Prompt / PowerShell:
```powershell
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

---

## Author
**Robert Ilie**  
Electronics & Telecommunications Engineering Graduate | National University of Science and Technology POLITEHNICA Bucharest
