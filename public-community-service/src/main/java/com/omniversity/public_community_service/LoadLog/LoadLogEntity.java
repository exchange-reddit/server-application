package com.omniversity.public_community_service.LoadLog;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name="load_logs")
@NoArgsConstructor
@AllArgsConstructor
public class LoadLogEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Long numOfCalls;

    private LocalDateTime recordedTime;

    private String sectionId;
}
