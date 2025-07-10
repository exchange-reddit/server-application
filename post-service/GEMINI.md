# Post-Service Codebase Analysis

This document provides an analysis of the Post-Service codebase, a Spring Boot application written in Java and built with Gradle.

## Project Structure

The project follows a standard Maven directory layout:

```
.
├── build.gradle
├── dockerfile
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── omniversity
    │   │           └── post_service
    │   │               ├── PostServiceApplication.java
    │   │               ├── config
    │   │               ├── controller
    │   │               ├── dto
    │   │               ├── entity
    │   │               ├── exception
    │   │               ├── kafka
    │   │               ├── mapper
    │   │               ├── repository
    │   │               └── service
    │   └── resources
    │       ├── application.yml
    │       ├── data.sql
    │       └── schema.sql
    └── test
        └── java
            └── com
                └── omniversity
                    └── post_service
```

### Key Components

*   **`build.gradle`**: Defines project dependencies, plugins, and build configurations. The service uses Spring Boot, Spring Web, Spring Data JPA, PostgreSQL driver, MapStruct for mapping, and Kafka.
*   **`dockerfile`**: For containerizing the application.
*   **`src/main/java`**: Contains the application's source code.
    *   **`PostServiceApplication.java`**: The main entry point of the Spring Boot application.
    *   **`config`**: `WebConfig.java` for web-related configurations (e.g., CORS).
    *   **`controller`**: `PostController.java` exposes RESTful endpoints for managing posts.
    *   **`dto`**: Data Transfer Objects used for API requests and responses.
        *   `input`: DTOs for creating and updating posts.
        *   `output`: DTOs for representing posts in API responses.
    *   **`entity`**: JPA entities representing the database schema. `Post.java` is the main entity.
    *   **`exception`**: Global and custom exception handlers.
    *   **`kafka/producer`**: `PostProducer.java` for sending messages to Kafka topics.
    *   **`mapper`**: MapStruct mappers for converting between DTOs and entities.
    *   **`repository`**: `PostRepository.java` is a Spring Data JPA repository for database operations on the `Post` entity.
    *   **`service`**: Business logic for the application.
        *   `PostService.java`: Core business logic for managing posts.
        *   `storage`: `StorageService.java` interface and `LocalStorageService.java` implementation for handling file storage.
*   **`src/main/resources`**:
    *   **`application.yml`**: Configuration file for the application, including database connection, Kafka, and server settings.
    *   **`schema.sql`**: Defines the database schema.
    *   **`data.sql`**: Contains initial data for the database.
*   **`src/test/java`**: Contains unit and integration tests.

## Architecture

The Post-Service follows a layered architecture:

1.  **Controller Layer**: `PostController` handles incoming HTTP requests, validates them, and delegates to the `PostService`.
2.  **Service Layer**: `PostService` contains the core business logic, orchestrating calls to the repository, mappers, and other services like Kafka and file storage.
3.  **Repository Layer**: `PostRepository` (an interface extending `JpaRepository`) handles all database interactions for the `Post` entity.
4.  **Domain/Entity Layer**: `Post` and `PostStatus` are JPA entities that model the application's domain.

### Key Features

*   **RESTful API**: Provides endpoints for CRUD (Create, Read, Update, Delete) operations on posts.
*   **File Storage**: Supports uploading and managing files associated with posts, using a local storage implementation.
*   **Asynchronous Communication**: Integrates with Kafka to publish events (e.g., when a post is created).
*   **Centralized Exception Handling**: `GlobalExceptionHandler` provides consistent error responses.
*   **DTOs and Mapping**: Uses DTOs to decouple the API from the internal data model, with MapStruct for efficient object mapping.
*   **Database Initialization**: `schema.sql` and `data.sql` are used to set up the database schema and seed it with initial data.

## How it Works

1.  A client sends an HTTP request to one of the endpoints in `PostController`.
2.  The controller validates the request and calls the appropriate method in `PostService`.
3.  The `PostService` executes the business logic, which may involve:
    *   Interacting with the database via `PostRepository`.
    *   Storing or retrieving files using `StorageService`.
    *   Publishing a message to a Kafka topic via `PostProducer`.
4.  The service layer returns a result to the controller.
5.  The controller maps the result to a response DTO and sends it back to the client.
