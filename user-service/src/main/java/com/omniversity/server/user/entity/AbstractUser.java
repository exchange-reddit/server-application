package com.omniversity.server.user.entity;

import com.omniversity.server.user.entity.Enums.*;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name="users")
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="user_type", discriminatorType = DiscriminatorType.STRING)
public abstract class AbstractUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", nullable = false, updatable = false)
    private long id;

    @Column(name="gender", nullable = false)
    private Gender gender;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="middle_name", nullable = true)
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    // Private email is an optional field and is not relevant to the user registration
    @Column(name="private_email", nullable = true, unique = true)
    private String privateEmail;

    // Private email verification status is an optional field and is not relevant to the user registration
    @Column(name = "private_email_verified")
    private boolean privateEmailVerified;

    @Column(name = "home_uni", nullable = false)
    private University homeUni;

    @Column(name = "home_email", nullable = false)
    private String homeEmail;

    @Column(name = "home_email_verified")
    private boolean homeEmailVerified;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "nationality", nullable = false)
    private Country nationality;

    @Column(name = "preferred_language", nullable = false)
    private Language preferredLanguage;

    @Column(name = "profile_picture", nullable = true)
    private String profilePicture;

    @Column(name = "program", nullable = false)
    private Program program;

    // Default value is false. Once the email is verified, the account status becomes active.
    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    protected AbstractUser () {}

    public AbstractUser(long id, Gender gender, String firstName, String middleName, String lastName, LocalDate dateOfBirth, String privateEmail, boolean privateEmailVerified, University homeUni, String homeEmail, boolean homeEmailVerified, String passwordHash, String userName, Country nationality, Language preferredLanguage, String profilePicture, Program program, boolean isActive, LocalDate registrationDate) {
        this.id = id;
        this.gender = gender;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.privateEmail = privateEmail;
        this.privateEmailVerified = privateEmailVerified;
        this.homeUni = homeUni;
        this.homeEmail = homeEmail;
        this.homeEmailVerified = homeEmailVerified;
        this.passwordHash = passwordHash;
        this.userName = userName;
        this.nationality = nationality;
        this.preferredLanguage = preferredLanguage;
        this.profilePicture = profilePicture;
        this.program = program;
        this.isActive = isActive;
        this.registrationDate= registrationDate;
    }
}
