package com.omniversity.server.user.entity;

import jakarta.persistence.*;

import java.util.Date;

// Applying properties of inheritance
@Entity
@Table(name="USER")
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private int id;

        @Column(name="USER_TYPE", nullable = false)
        private UserType userType;

        @Column(name="NAME", length=50, nullable = false)
        private String name;

        // Private Email: User's private email address
        @Column(name="PRIVATE_EMAIL", nullable = false, unique = true)
        private String privateEmail;

        @Column(name="PASSWORD_HASH", nullable = false)
        private String passwordHash;

        @Column(name="USER_ID", nullable = false, unique = true)
        private String userId;

        @Column(name="DATE_OF_BIRTH", nullable = false)
        private Date dateOfBirth;

        @Column(name="IS_ADMIN", nullable = false)
        private boolean isAdmin;

        // From here, information needed for Exchange_Users

        @Column(name="EXCHANGE_UNI", nullable = false)
        private University exchangeUni;

        @Column(name="HOME_UNI", nullable = false)
        private University homeUni;

        @Column(name="EXCHANGE_EMAIL", nullable = false, unique = true)
        private String exchangeEmail;

        @Column(name="HOME_EMAIL", nullable = false, unique = true)
        private String homeEmail;

        @Column(name="NATIONALITY", nullable = false)
        private Country nationality;

        @Column(name="EXCHANGE_START", nullable = false)
        private Date exchangeStart;

        @Column(name="EXCHANGE_END", nullable = false)
        private Date exchangeEnd;

        @Column(name="PREFERRED_LANGUAGE", nullable = false)
        private Language preferredLanguage;

        @Column(name="IS_ACTIVE", nullable = false)
        private Boolean isActive;

        public User() {}

        public User (int id, String name, String privateEmail,String passwordHash, String userId, Date dateOfBirth, boolean isAdmin, UserType userType, University homeUni, University exchangeUni, String exchangeEmail, String homeEmail, Country nationality, Date exchangeStart, Date exchangeEnd, Language preferredLanguage, Boolean isActive) {
                this.id = id;
                this.name = name;
                this.privateEmail = privateEmail;
                this.passwordHash = passwordHash;
                this.userId = userId;
                this.dateOfBirth = dateOfBirth;
                this.isAdmin = isAdmin;
                this.userType = userType;
                this.homeUni = homeUni;
                this.exchangeUni = exchangeUni;
                this.exchangeEmail = exchangeEmail;
                this.homeEmail = homeEmail;
                this.nationality = nationality;
                this.exchangeStart = exchangeStart;
                this.exchangeEnd =exchangeEnd;
                this.preferredLanguage = preferredLanguage;
                this.isActive = isActive;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getPrivateEmail() {
                return privateEmail;
        }

        public void setPrivateEmail(String privateEmail) {
                this.privateEmail = privateEmail;
        }

        public String getPasswordHash() {
                return passwordHash;
        }

        public void setPasswordHash(String passwordHash) {
                this.passwordHash = passwordHash;
        }

        public Date getDateOfBirth() {
                return dateOfBirth;
        }

        public void setDateOfBirth(Date dateOfBirth) {
                this.dateOfBirth = dateOfBirth;
        }

        public String getUserId() {
                return userId;
        }

        public void setUserId(String userId) {
                this.userId = userId;
        }

        public boolean isAdmin() {
                return isAdmin;
        }

        public void setAdmin(boolean admin) {
                this.isAdmin = admin;
        }

        public UserType getUserType() {
                return userType;
        }

        public void setUserType(UserType userType) {
                this.userType = userType;
        }

        public University getExchangeUni() {
                return exchangeUni;
        }

        public void setExchangeUni(University exchangeUni) {
                this.exchangeUni = exchangeUni;
        }

        public University getHomeUni() {
                return homeUni;
        }

        public void setHomeUni(University homeUni) {
                this.homeUni = homeUni;
        }

        public String getExchangeEmail() {
                return exchangeEmail;
        }

        public void setExchangeEmail(String exchangeEmail) {
                this.exchangeEmail = exchangeEmail;
        }

        public String getHomeEmail() {
                return homeEmail;
        }

        public void setHomeEmail(String homeEmail) {
                this.homeEmail = homeEmail;
        }

        public Country getNationality() {
                return nationality;
        }

        public void setNationality(Country nationality) {
                this.nationality = nationality;
        }

        public Date getExchangeStart() {
                return exchangeStart;
        }

        public void setExchangeStart(Date exchangeStart) {
                this.exchangeStart = exchangeStart;
        }

        public Date getExchangeEnd() {
                return exchangeEnd;
        }

        public void setExchangeEnd(Date exchangeEnd) {
                this.exchangeEnd = exchangeEnd;
        }

        public Language getPreferredLanguage() {
                return preferredLanguage;
        }

        public void setPreferredLanguage(Language preferredLanguage) {
                this.preferredLanguage = preferredLanguage;
        }

        public Boolean getActive() {
                return isActive;
        }

        public void setActive(Boolean active) {
                isActive = active;
        }
}



