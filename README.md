# Kotlin Betting API

This is a simple Kotlin-based REST API that allows players to:
- Register and authenticate
- View their wallet
- Place bets
- Retrieve bet transactions and results

The application leverages **Spring Boot** with an **H2 in-memory database** and exposes RESTful APIs.

---

## 📌 API Endpoints

| Method | Endpoint            | Input DTO       | Output DTO |
|--------|---------------------|-----------------|------------|
| POST   | `/auth/register`    | `UserRegisterDTO`| `UserReadDTO` |
| POST   | `/auth/authenticate`| `UserLoginDTO`  | `AuthTokenDTO` |
| POST   | `/api/bets`         | `BetTransactionCreateDTO`| `BetTransactionDTO` |
| GET    | `/api/bets`         | N/A             | `Page<BetTransactionDTO>` |
| GET    | `/api/me`           | N/A             | `UserReadDTO` |

---

## 🛠️ Usage Instructions

### 🏗️ Build & Test
To clean, build, and run tests, use:
```sh
mvn clean package
```

### 🚀 Deploy
To run the application:
```sh
mvn spring-boot:run
```

The application will start on **`http://localhost:8080`**.

### 🗄️ Database
- The application uses **H2 in-memory database** for storage.
- The H2 console is accessible at **`http://localhost:8080/h2-console`** (check `application.properties` for credentials).

### 🔍 API Testing with Postman
A **Postman collection** is available for testing the APIs. You can import it into Postman to easily interact with the endpoints.

---

## 🔗 Additional Information
- This project is built using **Kotlin, Spring Boot, and JPA**.
- Authentication is handled via **JWT tokens**.
- Ensure you include your **Bearer token** in API requests after authentication.

---

Feel free to fork and have fun. Happy coding! 🚀
