package com.omniversity.server.log.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name="logs")
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="auditType", discriminatorType = DiscriminatorType.STRING)
public abstract class AbstractLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="auditDate", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant auditDate;

    @Column(name="updateUser", nullable = false, updatable = false)
    private String updateUser;

    @Column(name = "updateIP", nullable = false, updatable = false)
    private String updateIP;

    @Column(name = "auditResult", nullable = false, updatable = false)
    private boolean auditResult;

    public AbstractLog(String updateUser, String updateIP) {
        this.updateUser = updateUser;
        this.updateIP = updateIP;
    }


}
