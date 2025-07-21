package com.omniversity.public_community_service.PublicCommunity.Entity;

import com.omniversity.public_community_service.PublicCommunity.Entity.enums.City;
import com.omniversity.public_community_service.PublicCommunity.Entity.enums.Country;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name="communities")
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="community_type", discriminatorType = DiscriminatorType.STRING)
public abstract class AbstractCommunity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id", nullable = false, updatable = false)
    private long id;

    @Column(name="name", nullable = false, updatable = false, unique = true)
    private String name;

    @Column(name="created_date", updatable = false)
    private LocalDate createdDate;

    @Column(name = "logo_image")
    private String logoImage;

    @Column(name = "background_image")
    private String backgroundImage;

    @Column(name = "description")
    private String description;

    @Column(name = "city")
    private City city;

    @Column(name = "country")
    private Country country;

    protected AbstractCommunity () {}

    public AbstractCommunity (Long id, String name, LocalDate createdDate, String logoImage, String backgroundImage, String description, City city, Country country) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.logoImage = logoImage;
        this.backgroundImage = backgroundImage;
        this.description = description;
        this.city = city;
        this.country = country;
    }
}
