package com.omniversity.server.user.entity;

import com.omniversity.server.user.entity.Enums.*;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("PROSPECTIVE_USER")
public class ProspectiveUser extends AbstractUser {

    @Column(name = "desired_university")
    private University desiredUniversity;

    public ProspectiveUser () {}

    public ProspectiveUser(long id, Gender gender, String firstName, String middleName, String lastName, LocalDate dateOfBirth, String privateEmail, boolean privateEmailVerified, University homeUni, String homeEmail, boolean homeEmailVerified, String passwordHash, String userName, Country nationality, Language preferredLanguage, String profilePicture, Program program, boolean isActive, LocalDate registrationDate, University desiredUniversity) {
        super(id, gender, firstName, middleName, lastName, dateOfBirth, privateEmail, privateEmailVerified, homeUni, homeEmail, homeEmailVerified, passwordHash, userName, nationality, preferredLanguage, profilePicture, program, isActive, registrationDate);
        this.desiredUniversity = desiredUniversity;
    }

    public University getDesiredUniversity () {
        return this.desiredUniversity;
    }

    public void setDesiredUniversity (University university) {
        this.desiredUniversity = university;
    }
}
