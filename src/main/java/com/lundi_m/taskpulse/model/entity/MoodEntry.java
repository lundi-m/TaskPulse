package com.lundi_m.taskpulse.model.entity;

import com.lundi_m.taskpulse.model.enums.EnergyLevel;
import com.lundi_m.taskpulse.model.enums.MoodType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "mood_entries")
public class MoodEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private TaskPulseUser user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MoodType moodType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnergyLevel energyLevel;

    @Column(nullable = false)
    private Integer availableTime;

    @Column(nullable = false, updatable = false)
    private Instant recordedAt = Instant.now();
}
