package com.omniversity.public_community_service.SectionPostDependency;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

// A dedicated database to maintain access control of different posts
@Entity
@Table(name = "dependencies")
@Data
public class PostSectionDependencyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    Long id;

    @Column(name = "created_date")
    LocalDateTime createdDate;

    @Column(name = "section_id")
    Long sectionId;

    @Column(name = "post_id")
    Long postId;
}
