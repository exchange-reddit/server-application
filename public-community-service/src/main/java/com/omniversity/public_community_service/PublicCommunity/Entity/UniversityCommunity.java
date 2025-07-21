package com.omniversity.public_community_service.PublicCommunity.Entity;

import com.omniversity.public_community_service.PublicCommunity.Entity.enums.City;
import com.omniversity.public_community_service.PublicCommunity.Entity.enums.Country;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("UNIVERSITY_COMMUNITY")
public class UniversityCommunity extends AbstractCommunity {
    @Column(name="email")
    private String email;

    @Column(name="website")
    private String website;

    @Column(name="phone_number")
    private String phoneNumber;

    public UniversityCommunity () {}

    public UniversityCommunity (long id, String name, LocalDate createdDate, String logoImage, String backgroundImage, String description, City city, Country country, String email, String website, String phoneNumber) {
        super(id, name, createdDate, logoImage, backgroundImage, description, city, country);
        this.email = email;
        this.website = website;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail () {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
