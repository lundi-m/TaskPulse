package com.lundi_m.taskpulse.repository;

import com.lundi_m.taskpulse.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByIdAndUserId(Long id, Long userId);

    @Query("""
            SELECT t FROM Task t
            WHERE t.user.id = :userId
            AND (:completed IS NULL OR t.completed = :completed)
            AND (:priority IS NULL OR t.priority = :priority)
            """)
    Page<Task> findFiltered(
            @Param("userId") Long userId,
            @Param("completed") Boolean completed,
            @Param("priority") Integer priority,
            Pageable pageable
    );
}
