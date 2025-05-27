package com.omniversity.post_service.entity;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Generates default constructor
@AllArgsConstructor // Generates all-args constructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant createdAt;
    private String title;
    private String content;
    private int authorId;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now(); // always UTC
    }
}
