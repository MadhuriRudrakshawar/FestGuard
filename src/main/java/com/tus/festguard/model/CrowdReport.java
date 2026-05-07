package com.tus.festguard.model;

import com.tus.festguard.enums.CrowdLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// CrowdReport.java
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "crowd_report")
public class CrowdReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private FestivalArea area;

    @Enumerated(EnumType.STRING)
    @Column(name = "crowd_level", nullable = false)
    private CrowdLevel crowdLevel;

    private String note;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

}


