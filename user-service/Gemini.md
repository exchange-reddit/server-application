# Project Structure: user-service

This document outlines the directory structure and key components of the `user-service` application.

## Top-Level

*   `build.gradle`: Gradle build configuration.
*   `dockerfile`: Docker image definition.
*   `gradlew`, `gradlew.bat`: Gradle wrapper scripts.
*   `.gitignore`: Specifies intentionally untracked files to ignore.

## Source Code (`src/main/java/com/omniversity/server/`)

*   **Core Application:**
    *   `ServerApplication.java`: Main Spring Boot application entry point.
    *   `JwtConfig.java`, `JwtTokenProvider.java`: JWT (JSON Web Token) configuration and utility classes for security.
*   **`config/`**: Application-wide configuration classes.
*   **`log/`**: Audit logging module.
    *   `LogRepository.java`: Data access for logs.
    *   `LogService.java`: Business logic for log management.
    *   `dto/`: Data Transfer Objects for log operations.
    *   `entity/`: JPA entities for various log types.
*   **`service/`**: General utility and security services.
    *   `HeaderExtractor.java`: Extracts information from HTTP headers.
    *   `PasswordValidator.java`: Validates user passwords.
    *   `SecurityConfig.java`: Spring Security configuration.
*   **`user/`**: User management module, segmented by user type.
    *   **`AllUser/`**: Common functionalities for all user types.
        *   `AbstractUserRepository.java`: Base repository for user data.
        *   `UserController.java`: Generic user API endpoints.
        *   `UserService.java`: Generic user business logic.
        *   `dto/`: Common DTOs for user requests/responses.
        *   `mapper/`: MapStruct mappers for common user DTOs.
    *   **`entity/`**: JPA entities for different user types and related enums.
        *   `AbstractUser.java`, `ExchangeUser.java`, `ProspectiveUser.java`: User entities.
        *   `Enums/`: Enumerations like `Country`, `Gender`, `UserType`.
    *   **`exception/`**: Custom exceptions for user-related errors.
    *   **`ExchangeUser/`**: Specific module for `ExchangeUser` operations.
        *   `ExchangeUserController.java`: API endpoints for exchange users.
        *   `ExchangeUserRepository.java`: Data access for exchange users.
        *   `ExchangeUserService.java`: Business logic for exchange users.
        *   `dto/`: DTOs specific to exchange users.
        *   `mapper/`: MapStruct mappers for exchange user DTOs.
    *   **`ProspectiveUser/`**: Specific module for `ProspectiveUser` operations.
        *   `ProspectiveUserController.java`: API endpoints for prospective users.
        *   `ProspectiveUserRepository.java`: Data access for prospective users.
        *   `ProspectiveUserService.java`: Business logic for prospective users.
        *   `dto/`: DTOs specific to prospective users.
        *   `mapper/`: MapStruct mappers for prospective user DTOs.

## Resources (`src/main/resources/`)

*   `application.properties`: Spring Boot application configuration.
*   `schema.sql`: Database schema definition.

## Tests (`src/test/java/com/omniversity/server/`)

*   Mirrors the `src/main/java` structure, containing unit and integration tests for each corresponding module (e.g., `log/`, `service/`, `user/`).