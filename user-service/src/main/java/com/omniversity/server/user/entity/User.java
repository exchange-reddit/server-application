package com.omniversity.server.user.entity;

import jakarta.persistence.*;

import java.util.Date;

// Applying properties of inheritance

/**
 * Notes:
 * Tried to set table name as user but turned out that user is a reserved keyword in psql.
 * Therefore, I set it as users.
 */
@Entity
@Table(name="users")

public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name="id", nullable = false)
        private long id;

        // Both prospective and exchange user accounts will be saved in the same db as prospective users shall be upgraded to exchange user account upon enrollment at the university of exchange.
        // Upon the submission of all datum marked as 'exchange user', the userType attribute will be changed to exchangeUser.
        @Column(name="user_type", nullable = false)
        private UserType userType;

        // Instead of saving first name and last name separately, the entire name will be saved in a single variable.
        @Column(name="name", length=50, nullable = false)
        private String name;

        // Private email will be required in addition to the home university email.
        // This choice has been made as there is a possibility of account loss when the user graduates from their home university.
        @Column(name="private_email", nullable = false, unique = true)
        private String privateEmail;

        // Check Private email verification status
        @Column(name="private_email_verified", nullable = false)
        private boolean privateEmailVerified;

        // Variable to help users connect with students from their home university who are planning to apply to the same university of exchange.
        @Column(name="home_uni", nullable = false)
        private University homeUni;

        // Home university email to assure that the user joining the platform actually is a university student.
        @Column(name="home_email", nullable = false, unique = true)
        private String homeEmail;

        // Check home uni email verification status
        @Column(name="home_email_verified", nullable = false)
        private boolean homeEmailVerified;

        @Column(name="password_hash", nullable = false)
        private String passwordHash;

        // User Id that would be displayed when the users write comments / posts.
        @Column(name="user_id", nullable = false, unique = true)
        private String userId;

        // Method to validate that the user is not too young or old.
        @Column(name="date_of_birth", nullable = false)
        private Date dateOfBirth;

        @Column(name="is_admin", nullable = false)
        private boolean isAdmin;

        // From here, information needed for Exchange_Users

        // Exchange university that the user will actually attend.
        @Column(name="exchange_uni")
        private University exchangeUni;

        // Exchange university email to assure that the user joining the platform actually is enrolling at the university as an exchange student.
        @Column(name="exchange_email", unique = true)
        private String exchangeEmail;

        // Check exchange email verification status
        @Column(name="exchange_email_verified", nullable = false)
        private boolean exchangeEmailVerified;

        @Column(name="nationality")
        private Country nationality;

        // Date to initiate the user account's active exchange user status
        @Column(name="exchange_start")
        private Date exchangeStart;

        // Date to terminate the user account's active exchange user status.
        @Column(name="exchange_end")
        private Date exchangeEnd;

        // Mainly used for community recommendation
        @Column(name="preferred_language")
        private Language preferredLanguage;

        @Column(name="profile_picture")
        private String profilePicture;

        @Column(name="is_active")
        private Boolean isActive;

        public User() {}

        public User (int id, String name, String privateEmail, Boolean privateEmailVerified, String passwordHash, String userId, Date dateOfBirth, boolean isAdmin, UserType userType, University homeUni, University exchangeUni, String exchangeEmail, Boolean exchangeEmailVerified, String homeEmail, Boolean homeEmailVerified, Country nationality, Date exchangeStart, Date exchangeEnd, Language preferredLanguage, String profilePicture, Boolean isActive) {
                this.id = id;
                this.name = name;
                this.privateEmail = privateEmail;
                this.privateEmailVerified = false;
                this.passwordHash = passwordHash;
                this.userId = userId;
                this.dateOfBirth = dateOfBirth;
                this.isAdmin = isAdmin;
                this.userType = userType;
                this.homeUni = homeUni;
                this.homeEmailVerified = false;
                this.exchangeUni = exchangeUni;
                this.exchangeEmail = exchangeEmail;
                this.exchangeEmailVerified = false;
                this.homeEmail = homeEmail;
                this.nationality = nationality;
                this.exchangeStart = exchangeStart;
                this.exchangeEnd = exchangeEnd;
                this.preferredLanguage = preferredLanguage;
                this.profilePicture = profilePicture;
                this.isActive = isActive;
        }

        public long getId() {
                return id;
        }

        public void setId(long id) {
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

        public String getProfilePicture() {
                return profilePicture;
        }

        public void setProfilePicture(String profilePicture) {
                this.profilePicture = profilePicture;
        }

        public Boolean getActive() {
                return isActive;
        }

        public void setActive(Boolean active) {
                isActive = active;
        }

        public void setEmailVerified(Boolean verified, int emailType) {
                switch (emailType) {
                        case (1):
                                privateEmailVerified = verified;
                                break;
                        case (2):
                                homeEmailVerified = verified;
                                break;
                        case (3):
                                exchangeEmailVerified = verified;
                                break;
                }
        }

}



