package com.lundi_m.taskpulse.service;

import com.lundi_m.taskpulse.dto.TaskRequest;
import com.lundi_m.taskpulse.dto.TaskResponse;
import com.lundi_m.taskpulse.model.Task;
import com.lundi_m.taskpulse.model.TaskPulseUser;
import com.lundi_m.taskpulse.repository.TaskRepository;
import com.lundi_m.taskpulse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskResponse createTask(String email, TaskRequest request){
        TaskPulseUser user= userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Task task = Task.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .estimatedDuration(request.getEstimatedDuration())
                .difficultyLevel(request.getDifficultyLevel())
                .deadline(request.getDeadline())
                .completed("Not completed")
                .createdAt(Instant.now())
                .build();

        Task saved = taskRepository.save(task);

        return TaskResponse.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .description(saved.getDescription())
                .priority(saved.getPriority())
                .estimatedDuration(saved.getEstimatedDuration())
                .difficultyLevel(saved.getDifficultyLevel())
                .deadline(saved.getDeadline())
                .completed(saved.getCompleted())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}
