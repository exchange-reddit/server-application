package com.omniversity.public_community_service.Section.Entity;

import com.omniversity.public_community_service.PublicCommunity.Entity.AbstractCommunity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="sections")
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="section_type", discriminatorType = DiscriminatorType.STRING)
public abstract class AbstractSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="public_community_id", nullable = false)
    private AbstractCommunity community;

    protected AbstractSection () {}

    public AbstractSection (Long id, AbstractCommunity community) {
        this.id = id;
        this.community = community;
    }
}
