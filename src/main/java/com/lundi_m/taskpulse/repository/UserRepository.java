package com.lundi_m.taskpulse.repository;

import com.lundi_m.taskpulse.model.TaskPulseUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<TaskPulseUser, Long> {

    Optional<Long> findByEmail(String email);
    boolean existsByEmail(String email);
}
