package com.lundi_m.taskpulse.model;

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

    @Column(nullable = false)
    private Integer priority; // 1-5

    @Column(nullable = false)
    private Integer estimatedDuration; // minutes

    @Column(nullable = false)
    private Integer difficultyLevel; // 1-5

    private LocalDate deadline;

    @Column(nullable = false)
    private String completed;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(user, task.user) && Objects.equals(title, task.title) && Objects.equals(description, task.description) && Objects.equals(priority, task.priority) && Objects.equals(estimatedDuration, task.estimatedDuration) && Objects.equals(difficultyLevel, task.difficultyLevel) && Objects.equals(deadline, task.deadline) && Objects.equals(completed, task.completed) && Objects.equals(createdAt, task.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, title, description, priority, estimatedDuration, difficultyLevel, deadline, completed, createdAt);
    }
}
