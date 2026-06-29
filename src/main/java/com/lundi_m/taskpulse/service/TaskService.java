package com.lundi_m.taskpulse.service;

import com.lundi_m.taskpulse.dto.TaskRequest;
import com.lundi_m.taskpulse.dto.TaskResponse;
import com.lundi_m.taskpulse.model.Task;
import com.lundi_m.taskpulse.model.TaskPulseUser;
import com.lundi_m.taskpulse.repository.TaskRepository;
import com.lundi_m.taskpulse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        return mapToDTO(saved);
    }

    public Page<TaskResponse> getTasks(String email,
                               String completed,
                               Integer priority,
                               Pageable pageable){

        TaskPulseUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Page<Task> tasks = taskRepository.findFiltered(user.getId(), completed ,priority, pageable);

        return tasks.map(this::mapToDTO);
    }

    public Task getTaskById(String email, Long taskId){
        TaskPulseUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                return taskRepository.findByIdAndUserId(taskId ,user.getId())
                        .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public TaskResponse updateTask(String email, Long taskId, TaskRequest request){
        Task task = getTaskById(email, taskId);

        task = Task.builder()
                .user(task.getUser())
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

        return mapToDTO(saved);
    }

    public void deleteTask(String email, Long taskId){
        Task task = getTaskById(email, taskId);
        taskRepository.delete(task);
    }

    public Task markComplete(String email, Long taskId){
        Task task = getTaskById(email, taskId);
        task.setCompleted("Completed");

        return taskRepository.save(task);
    }

    // Mapper
    private TaskResponse mapToDTO(Task task){
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority())
                .estimatedDuration(task.getEstimatedDuration())
                .difficultyLevel(task.getDifficultyLevel())
                .deadline(task.getDeadline())
                .completed(task.getCompleted())
                .createdAt(task.getCreatedAt())
                .build();
    }

}
