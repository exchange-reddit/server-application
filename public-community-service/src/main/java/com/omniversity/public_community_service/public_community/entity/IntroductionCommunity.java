package com.omniversity.public_community_service.public_community.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("introduction_community")
public class IntroductionCommunity extends AbstractPublicCommunity {

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "landing_photo")
    private String landingPhoto;

}
