package com.omniversity.server.user.entity;

import com.omniversity.server.user.entity.Enums.*;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("EXCHANGE_USER")
public class ExchangeUser extends AbstractUser {

    @Column(name = "exchange_university")
    private University exchangeUniversity;

    @Column(name = "exchange_email", unique = true)
    private String exchangeEmail;

    @Column(name = "exchange_email_verified")
    private boolean exchangeEmailVerified;

    @Column(name="exchange_start")
    private LocalDate exchangeStart;

    @Column(name = "exchange_end")
    private LocalDate exchangeEnd;

    public ExchangeUser () {}

    public ExchangeUser(long id, Gender gender, String firstName, String middleName, String lastName, LocalDate dateOfBirth, String privateEmail, boolean privateEmailVerified, University homeUni, String homeEmail, boolean homeEmailVerified, String passwordHash, String userName, Country nationality, Language preferredLanguage, String profilePicture, Program program, boolean isActive, LocalDate registrationDate, University exchangeUniversity, String exchangeEmail, boolean exchangeEmailVerified, LocalDate exchangeStart, LocalDate exchangeEnd) {
        super(id, gender, firstName, middleName, lastName, dateOfBirth, privateEmail, privateEmailVerified, homeUni, homeEmail, homeEmailVerified, passwordHash, userName, nationality, preferredLanguage, profilePicture, program, isActive, registrationDate);
        this.exchangeUniversity = exchangeUniversity;
        this.exchangeEmail = exchangeEmail;
        this.exchangeEmailVerified = exchangeEmailVerified;
        this.exchangeStart = exchangeStart;
        this.exchangeEnd = exchangeEnd;
    }

    public University getExchangeUni() {
        return this.exchangeUniversity;
    }

    public void setExchangeUni (University exchangeUni) {
        this.exchangeUniversity = exchangeUni;
    }

    public String getExchangeEmail () {
        return this.exchangeEmail;
    }

    public void setExchangeEmail (String email) {
        this.exchangeEmail = email;
    }

    public boolean getExchangeEmailVerified () {
        return this.exchangeEmailVerified;
    }

    public void setExchangeEmailVerified (boolean verified) {
        this.exchangeEmailVerified = verified;
    }

    public LocalDate getExchangeStart () {
        return this.exchangeStart;
    }

    public void setExchangeStart (LocalDate date) {
        this.exchangeStart = date;
    }

    public LocalDate getExchangeEnd () {
        return this.exchangeEnd;
    }

    public void setExchangeEnd (LocalDate date) {
        this.exchangeEnd = date;
    }
}
