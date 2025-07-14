package com.omniversity.public_community_service.public_community.entity;

import com.omniversity.public_community_service.public_community.entity.enums.City;
import com.omniversity.public_community_service.public_community.entity.enums.Country;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="public_communities")
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="public_community_type", discriminatorType = DiscriminatorType.STRING)
public abstract class AbstractPublicCommunity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "country", nullable = false)
    private Country country;

    @Column(name = "city", nullable = false)
    private City city;

    @Column(name = "description")
    private String description;

    // TODO: Add a connection between Post Service

}
