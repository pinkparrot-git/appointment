
# Appointment Service

A **Spring Boot** application that provides a simple REST API for managing **patients** and their **appointments**. This project is part of a coding challenge and is intended to be reviewed, tested, and improved.

---

## ⚙️ Features

- RESTful API for:
  - Creating and retrieving patients
  - Scheduling and viewing appointments
- Layered architecture (Controller, Service, Repository)
- DTO and entity mapping
- Integration tests
- Swagger/OpenAPI support

---

## 🗂️ Technologies

- Java 17+
- Spring Boot
- Spring Data JPA (with H2 in-memory database)
- Gradle (build system)
- JUnit 5 & Mockito
- Swagger for API docs

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Gradle (or use the Gradle Wrapper)

### Run the Application

```bash
./gradlew bootRun
```

The app will be available at:  
```
http://localhost:8080
```

### Swagger UI (API Docs)

Once running, explore the endpoints via Swagger:

```
http://localhost:8080/swagger-ui/index.html
```

---

## 📁 Key Entities

### Patient

- `id` — unique identifier
- `name` — patient's full name
- `ssn` — unique social security number
- `appointments` — list of appointments for this patient

### Appointment

- `id` — unique identifier
- `reason` — reason for the appointment
- `date` — scheduled date/time
- `patient` — linked patient

---

## ✅ Example API Calls

**Create Patient**
```http
POST /api/patients
```
```json
{
  "name": "John Doe",
  "ssn": "123-45-6789"
}
```

**Schedule Appointment**
```http
POST /api/appointments
```
```json
{
  "patientId": 1,
  "reason": "Follow-up",
  "date": "2025-08-01T10:00:00"
}
```

---

## 🧪 Testing

To run all tests:
```bash
./gradlew test
```

---

## 💡 Areas for Improvement

- Add input validation (e.g., using `@Valid`)
- Implement proper exception handling with `@ControllerAdvice`
- Add pagination for list endpoints
- Improve mapper structure or use MapStruct
- Introduce service interfaces and improve separation of concerns
- Secure the API (Spring Security, role-based access)

---

## 📝 License

This project was created for educational and assessment purposes.
