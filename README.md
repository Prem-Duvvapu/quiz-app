# Quiz App Microservices Project

A Spring Boot-based microservices application that allows users to take quizzes categorized by topics. The application consists of two primary microservices—`question-service` and `quiz-service`—registered via a centralized `service-registry` using Netflix Eureka. The app follows a modular architecture with service-to-service communication powered by OpenFeign.

---

## 🛠️ Tech Stack

- **Spring Boot** – Java application framework
- **Spring Data JPA** – ORM for data persistence
- **PostgreSQL** – Relational database for storing questions/quizzes
- **Spring Cloud Netflix Eureka** – Service registry
- **Spring Cloud OpenFeign** – Declarative REST client for inter-service communication
- **Lombok** – Reduces boilerplate code
- **JUnit** – Testing framework

---

## 🧩 Microservices Overview

### 1. **Question Service**
This service is responsible for managing quiz questions. It handles CRUD operations on questions and provides APIs to fetch questions based on category, generate question IDs for a quiz, and evaluate submitted answers.

**Endpoints:**

| Method | URL | Description |
|--------|-----|-------------|
| `GET` | `/question/allQuestions` | Get all questions |
| `GET` | `/question/category/{category}` | Get questions by category |
| `POST` | `/question/add` | Add a new question |
| `POST` | `/question/update` | Update an existing question |
| `GET` | `/question/generate?category={category}&numQuestions={n}` | Generate a list of question IDs for quiz creation |
| `POST` | `/question/getQuestions` | Get full question details by a list of IDs |
| `POST` | `/question/getScore` | Submit answers and get the score |

**Question Data Model Includes:**
- `question`: The quiz question
- `option1` to `option4`: Four possible answer options
- `rightAnswer`: The correct option
- `category`: The topic/category of the question

---

### 2. **Quiz Service**
This service is responsible for creating and managing quizzes. It communicates with `question-service` to fetch questions for a specific category and number, and handles quiz submission and scoring.

**Endpoints:**

| Method | URL | Description |
|--------|-----|-------------|
| `POST` | `/quiz/create` | Create a new quiz by providing category, number of questions, and title |
| `GET` | `/quiz/get/{id}` | Retrieve all questions of a quiz by quiz ID |
| `POST` | `/quiz/submit/{id}` | Submit quiz answers and receive a score |

**Quiz Creation Flow:**
1. Client calls `/quiz/create` with category, number of questions, and quiz title.
2. `quiz-service` internally calls `question-service` to get random question IDs.
3. The quiz is created and stored with those question references.
4. When a user takes the quiz, their responses are sent to `/quiz/submit/{id}` for scoring.

---

### 3. **Service Registry (Eureka Server)**
The service registry enables service discovery. Both `quiz-service` and `question-service` register themselves to Eureka so they can locate each other without hardcoded URLs.

---

