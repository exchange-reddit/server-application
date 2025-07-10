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

## Unit Testing with JUnit

The `user-service` employs JUnit 5 for its unit and integration tests, leveraging Mockito for mocking dependencies.

### Service Layer Tests (e.g., `ProspectiveUserServiceTest.java`, `PasswordValidatorTest.java`)

*   **Mockito Integration**: Utilize `@ExtendWith(MockitoExtension.class)` to enable Mockito annotations.
*   **Dependency Mocking**: Dependencies are mocked using `@Mock` (e.g., repositories, other services, mappers, `PasswordEncoder`, `PasswordValidator`).
*   **Injection**: The class under test is instantiated with injected mocks using `@InjectMocks`.
*   **Setup**: `@BeforeEach` is used for common setup, such as initializing test data.
*   **Mock Behavior**: `when().thenReturn()` is extensively used to define the behavior of mocked dependencies.
*   **Assertions**: Assertions like `assertEquals`, `assertTrue`, `assertFalse`, and `assertThrows` are used to verify method outcomes and exception handling.

### Controller Layer Tests (e.g., `ProspectiveUserControllerTest.java`)

*   **Mockito Integration**: Also use `@ExtendWith(MockitoExtension.class)` for Mockito.
*   **Service Mocking**: The service layer dependency is mocked using `@Mock`.
*   **Injection**: The controller under test is instantiated with injected mocks using `@InjectMocks`.
*   **Setup**: `@BeforeEach` is used for setting up common test data.
*   **Mock Behavior**: Service method calls are mocked to control their return values or to throw specific exceptions.
*   **Assertions**: Assertions focus on verifying HTTP status codes (`HttpStatus.CREATED`, `HttpStatus.CONFLICT`, `HttpStatus.BAD_REQUEST`, `HttpStatus.NOT_FOUND`, `HttpStatus.INTERNAL_SERVER_ERROR`) and the content of the `ResponseEntity` body.
*   **Error Handling**: Tests cover successful scenarios as well as various error conditions, including custom exceptions like `UserAlreadyExistsException`, `WeakPasswordException`, and `NoSuchUserException`.

### Entity/DTO Tests (e.g., `ExchangeUserTest.java`, `ProspectiveUserRegistrationDtoTest.java`)

*   **Plain JUnit**: These are plain JUnit tests without Mockito, as they test simple POJOs (Plain Old Java Objects).
*   **Setup**: `@BeforeEach` is used to initialize the entity or DTO object.
*   **Assertions**: Tests primarily verify the correct functioning of constructors, getters, and setters using `assertEquals`.
