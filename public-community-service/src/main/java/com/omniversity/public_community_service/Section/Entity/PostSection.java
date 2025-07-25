package com.omniversity.public_community_service.Section.Entity;

import com.omniversity.public_community_service.PublicCommunity.Entity.AbstractCommunity;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("POST_SECTION")
public class PostSection extends AbstractSection{
    @Column(name = "thumbnail_post_one")
    private String thumbNailPostOne;

    @Column(name="thumbnail_post_two")
    private String thumbNailPostTwo;

    @Column(name="thumbnail_post_three")
    private String thumbNailPostThree;

    public PostSection () {}

    public PostSection (Long id, AbstractCommunity abstractCommunity, String thumbNailPostOne, String thumbNailPostTwo, String thumbNailPostThree) {
        super(id, abstractCommunity);
        this.thumbNailPostOne = thumbNailPostOne;
        this.thumbNailPostTwo = thumbNailPostTwo;
        this.thumbNailPostThree = thumbNailPostThree;
    }

    public String getThumbNailPostOne() {
        return thumbNailPostOne;
    }

    public void setThumbNailPostOne(String thumbNailPostOne) {
        this.thumbNailPostOne = thumbNailPostOne;
    }

    public String getThumbNailPostTwo() {
        return thumbNailPostTwo;
    }

    public void setThumbNailPostTwo(String thumbNailPostTwo) {
        this.thumbNailPostTwo = thumbNailPostTwo;
    }

    public String getThumbNailPostThree() {
        return thumbNailPostThree;
    }

    public void setThumbNailPostThree(String thumbNailPostThree) {
        this.thumbNailPostThree = thumbNailPostThree;
    }
}
