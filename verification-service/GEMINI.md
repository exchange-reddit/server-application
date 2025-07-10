# Verification Service Code Summary

This document provides a detailed summary of the `verification-service` application's source code.

## 1. Main Application

### `ServerApplication.java`

*   **Purpose:** This is the main entry point for the Spring Boot application.
*   **Key Features:**
    *   It uses the `@SpringBootApplication` annotation to enable auto-configuration and component scanning.
    *   It excludes the default security auto-configuration (`SecurityAutoConfiguration.class`), which means the application does not have any default authentication or authorization mechanisms enabled out of the box.

## 2. Email Service

This service is responsible for sending emails.

### `EmailService.java` (Interface)

*   **Purpose:** Defines the contract for sending emails.
*   **Method:**
    *   `sendMail(AbstractEmailContext email)`: Sends an email based on the provided email context.

### `AbstractEmailContext.java` (Abstract Class)

*   **Purpose:** Provides a base template for creating different types of email contexts.
*   **Key Features:**
    *   It defines common email attributes like `from`, `to`, `subject`, `templateLocation`, etc.
    *   It uses a `Map` to store context variables that can be used to populate email templates.

### `DefaultEmailContext.java`

*   **Purpose:** Implements the `EmailService` interface and provides the default logic for sending emails.
*   **Key Features:**
    *   It uses `JavaMailSender` to send emails.
    *   It integrates with `SpringTemplateEngine` (Thymeleaf) to process email templates.
    *   The `sendMail` method constructs a `MimeMessage` and uses a `MimeMessageHelper` to set the email details.
    *   It processes the Thymeleaf template specified in the `AbstractEmailContext` to generate the email content.

### `DefaultAccountVerificationContext.java`

*   **Purpose:** A specialized email context for sending account verification emails.
*   **Key Features:**
    *   It extends `AbstractEmailContext`.
    *   The `init` method sets the template location, subject, and sender for the verification email.
    *   It has a `setToken` method to add the verification token to the email context.

## 3. Registration Token

This part of the application handles the creation, validation, and management of registration tokens.

### `RegistrationTokenController.java`

*   **Purpose:** Exposes REST endpoints for token-related operations.
*   **Endpoints:**
    *   `GET /token/email`: Requests a new verification token to be sent to the user's email.
        *   It first checks for and deletes any existing tokens for that email.
        *   It then creates a new token and sends it via email.
    *   `POST /token/validate`: Validates a given token.
        *   It checks if the token is valid and not expired.
        *   If the token is valid, it is deleted from the database.

### `RegistrationTokenService.java`

*   **Purpose:** Contains the business logic for managing registration tokens.
*   **Key Methods:**
    *   `createRegistrationToken()`: Generates a new `RegistrationToken` with a random 5-digit code and an expiration time of 15 minutes.
    *   `saveRegistrationToken()`: Saves a token to the database.
    *   `checkDuplicate()`: Checks for and deletes any existing tokens for a given email before creating a new one.
    *   `sendRegistrationToken()`: Constructs and sends the verification email using the `EmailService`.
    *   `findByToken()`: Retrieves a token by its code.
    *   `verifyToken()`: Verifies the provided token against the one stored in the database. It checks for matching code, email, and verification type, and also ensures the token has not expired.
    *   `removeToken()`: Deletes a token from the database.

### `RegistrationTokenRepository.java` (Interface)

*   **Purpose:** A Spring Data JPA repository for `RegistrationToken` entities.
*   **Key Methods:**
    *   `findByCode()`: Finds a token by its code.
    *   `findByEmail()`: Finds a token by the user's email.
    *   `findByCodeAndEmail()`: Finds a token by a combination of code and email.
    *   `removeByCode()`: Deletes a token by its code.

### `RegistrationToken.java` (Entity)

*   **Purpose:** Represents the `registrationToken` table in the database.
*   **Attributes:**
    *   `id`: The primary key.
    *   `code`: The verification code.
    *   `expiryDate`: The date and time when the token expires.
    *   `email`: The email address the token was sent to.
    *   `verificationType`: An integer representing the type of verification.

### DTOs

*   **`RegistrationDto.java`:** A data transfer object for requesting a new registration token. It contains the user's `name`, `email`, and `verificationType`.
*   **`VerificationDto.java`:** A data transfer object for verifying a token. It contains the `code`, `email`, and `verificationType`.

### Exceptions

*   **`InvalidTokenException.java`:** A custom exception thrown when a token is invalid (e.g., wrong code, expired).
*   **`NoSuchTokenException.java`:** A custom exception thrown when a token is not found in the database.

## 4. Resources

### `application.properties`

*   **Purpose:** Contains the configuration for the application.
*   **Key Properties:**
    *   `server.port`: The port the application runs on (8082).
    *   `spring.datasource.*`: Database connection settings (URL, username, password).
    *   `spring.mail.*`: SMTP server settings for sending emails.
    *   `token.validity.seconds`: The validity period of the registration token (900 seconds = 15 minutes).
    *   `spring.thymeleaf.*`: Thymeleaf template engine configuration.

### `schema.sql`

*   **Purpose:** Defines the schema for the `registration_token` table.
*   **Table Columns:**
    *   `id`
    *   `code`
    *   `expiry_date`
    *   `email`
    *   `verificationType`

### `email_verification_en.html`

*   **Purpose:** A Thymeleaf template for the email verification email.
*   **Key Features:**
    *   It displays a personalized greeting to the user.
    *   It shows the verification code.
    *   It includes a button to go to the application.

## 5. Tests

### `VerificationServiceApplicationTests.java`

*   **Purpose:** A basic Spring Boot integration test that checks if the application context loads successfully.
*   **Key Annotations:**
    *   `@SpringBootTest`: Provides a convenient way to start up an application context for testing.
*   **Test Method:**
    *   `contextLoads()`: A simple test method annotated with `@Test` that asserts the context loads without exceptions. This is a common pattern to ensure the basic setup of the Spring Boot application is correct.

### `RegistrationTokenServiceTest.java`

*   **Purpose:** A unit test for the `RegistrationTokenService`.
*   **Key Features:**
    *   It uses Mockito to mock the `RegistrationTokenRepository` and `EmailService`.
    *   It sets up a test `RegistrationToken` object in the `setUp` method using `@BeforeEach`.
    *   **Note:** This test file is currently incomplete and does not contain any actual test methods (`@Test` annotated methods). It serves as a placeholder for future unit tests.