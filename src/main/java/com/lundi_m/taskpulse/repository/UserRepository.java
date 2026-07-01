package com.lundi_m.taskpulse.repository;

import com.lundi_m.taskpulse.model.entity.TaskPulseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<TaskPulseUser, Long> {

    Optional<TaskPulseUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
