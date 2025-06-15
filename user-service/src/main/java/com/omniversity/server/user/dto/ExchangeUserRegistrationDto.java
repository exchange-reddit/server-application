package com.omniversity.server.user.dto;

import com.omniversity.server.user.entity.*;
import jakarta.validation.constraints.*;

import java.util.Date;

/**
 * TODO:
 * Set a custom annotation for 'exchange university email', and 'home university email'.
 * Consider migrating dto to a record rather than a class.
 * A custom validator for date of birth to constrain user with a certain age.
 */
public class ExchangeUserRegistrationDto {
    private UserType userType;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    @NotBlank(message = "Private email must be provided")
    @Email(message = "Email should be valid")
    private String privateEmail;
    @NotBlank(message = "Password must be provided")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    @NotBlank(message = "User ID must be provided")
    private String userId;
    @NotBlank(message = "Your date of birth must be provided")
    private Date dateOfBirth;
    private Boolean isAdmin;
    @NotBlank(message = "You must provide the university of exchange")
    private University exchangeUni;
    @NotBlank(message = "You must provide your home university")
    private University homeUni;
    @NotBlank(message = "You must provide your exchange university email")
    @Email(message = "Email should be valid")
    private String exchangeEmail;
    @NotBlank(message = "You must provide your home university email")
    @Email(message = "Email should be valid")
    private String homeEmail;
    @NotBlank(message = "You must provide your nationality")
    private Country nationality;
    @NotBlank(message = "Date of exchange start must be provided")
    private Date exchangeStart;
    @NotBlank(message = "Date of exchange end must be provided")
    private Date exchangeEnd;
    @NotBlank(message = "Preferred language must be provided")
    private Language preferredLanguage;
    @NotBlank(message = "User program must be provided")
    private Program program;
    private String profilePicture;
    private Boolean isActive;


    public ExchangeUserRegistrationDto(UserType userType, String name, String privateEmail, String password, String userId, Date dateOfBirth, Boolean isAdmin, University exchangeUni, University homeUni, String exchangeEmail, String homeEmail, Country nationality, Date exchangeStart, Date exchangeEnd, Language preferredLanguage, String profilePicture, Program program, Boolean isActive) {
        this.userType = userType;
        this.name = name;
        this.privateEmail = privateEmail;
        this.password = password;
        this.userId = userId;
        this.dateOfBirth = dateOfBirth;
        this.isAdmin = isAdmin;
        this.exchangeUni = exchangeUni;
        this.homeUni = homeUni;
        this.exchangeEmail = exchangeEmail;
        this.homeEmail = homeEmail;
        this.nationality = nationality;
        this.exchangeStart = exchangeStart;
        this.exchangeEnd = exchangeEnd;
        this.preferredLanguage = preferredLanguage;
        this.profilePicture = profilePicture;
        this.program = program;
        this.isActive = isActive;
    }

    public UserType getUserType() {
        return userType;
    }


    public String getName() {
        return name;
    }


    public String getPrivateEmail() {
        return privateEmail;
    }


    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public University getExchangeUni() {
        return exchangeUni;
    }

    public University getHomeUni() {
        return homeUni;
    }

    public String getExchangeEmail() {
        return exchangeEmail;
    }

    public String getHomeEmail() {
        return homeEmail;
    }

    public Country getNationality() {
        return nationality;
    }

    public Date getExchangeStart() {
        return exchangeStart;
    }

    public Date getExchangeEnd() {
        return exchangeEnd;
    }

    public Language getPreferredLanguage() {
        return preferredLanguage;
    }

    public String getProfilePicture() { return profilePicture; }
    public Program getProgram() { return program; }
    public Boolean getActive() {
        return isActive;
    }
}

