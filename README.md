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
| GET    | `/api/leaderboard`  | N/A             | `List<LeaderboardDTO>` |

---

## 📚 DTOs (Data Transfer Objects)

### 1. **UserRegisterDTO**
The `UserRegisterDTO` is used to capture the necessary data for user registration.

#### Fields:
- `username` (String, Required): The user's chosen username.
- `password` (String, Required): The user's password.
- `firstName` (String, Required): The user's first name.
- `lastName` (String, Required): The user's last name.

### 2. **UserLoginDTO**
The `UserLoginDTO` is used to capture the necessary data for user sign-in.

#### Fields:
- `username` (String, Required): The user's chosen username.
- `password` (String, Required): The user's password.

### 3. **BetTransactionCreateDTO**
The `BetTransactionCreateDTO` is used to capture the necessary data to create a bet.

#### Fields:
- `betAmount` (Double, Required, Min(1)): The user's chosen bet amount.
- `betNumber` (String, Required, Min(1), Max(10)): The user's bet number.

---

## 🛠️ Usage Instructions

### 🏗️ Build & Test
To clean, build, and run tests, use:
```sh
./gradlew clean build
```

### 🚀 Deploy
To run the application:
```sh
./gradlew bootRun
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
- Ensure you include your **Bearer token** in API requests after authentication (a script after authenticate should manage it automatically).

---

Feel free to fork and have fun. Happy coding! 🚀
