package com.lundi_m.taskpulse.model.entity;

import com.lundi_m.taskpulse.model.enums.DifficultyLevel;
import com.lundi_m.taskpulse.model.enums.Priority;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private TaskPulseUser user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Column(nullable = false)
    private Integer estimatedDuration; // minutes

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DifficultyLevel difficultyLevel;

    private LocalDate deadline;

    @Column(nullable = false)
    private String completed;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    private Instant completeAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(user, task.user) && Objects.equals(title, task.title) && Objects.equals(description, task.description) && priority == task.priority && Objects.equals(estimatedDuration, task.estimatedDuration) && difficultyLevel == task.difficultyLevel && Objects.equals(deadline, task.deadline) && Objects.equals(completed, task.completed) && Objects.equals(createdAt, task.createdAt) && Objects.equals(completeAt, task.completeAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, title, description, priority, estimatedDuration, difficultyLevel, deadline, completed, createdAt, completeAt);
    }
}
