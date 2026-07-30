package com.lundi_m.taskpulse.testUtil;

import com.lundi_m.taskpulse.dto.task.TaskRequest;
import com.lundi_m.taskpulse.dto.task.TaskResponse;
import com.lundi_m.taskpulse.model.entity.Task;
import com.lundi_m.taskpulse.model.enums.DifficultyLevel;
import com.lundi_m.taskpulse.model.enums.Priority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class TaskData {

    public static Task createTask(String title,
                            DifficultyLevel difficulty,
                            Priority priority,
                            int estimatedDuration,
                            LocalDate deadline) {
        return Task.builder()
                .title(title)
                .difficultyLevel(difficulty)
                .priority(priority)
                .estimatedDuration(estimatedDuration)
                .deadline(deadline)
                .completed("Not completed")
                .build();
    }

    public static TaskRequest createTaskRequest(){
        TaskRequest request = new TaskRequest();

        request.setTitle("Study");
        request.setDescription("Study for the upcoming test");
        request.setPriority(Priority.HIGH);
        request.setEstimatedDuration(120);
        request.setDifficultyLevel(DifficultyLevel.MEDIUM);
        request.setDeadline(LocalDate.now().plusDays(7));

        return request;
    }

    public static TaskResponse createTaskResponse(){
        return TaskResponse.builder()
                .id(1L)
                .title("Study")
                .description("Study for the upcoming test")
                .priority(Priority.HIGH)
                .estimatedDuration(120)
                .difficultyLevel(DifficultyLevel.MEDIUM)
                .deadline(LocalDate.now().plusDays(7))
                .completed("Not completed")
                .createdAt(Instant.now())
                .completedAt(null)
                .build();
    }

    public static TaskResponse createTaskResponse2(){
        return TaskResponse.builder()
                .id(12L)
                .title("Assignment")
                .description("Finish and submit the assignment before the due date")
                .priority(Priority.HIGH)
                .estimatedDuration(180)
                .difficultyLevel(DifficultyLevel.MEDIUM)
                .deadline(LocalDate.now().plusDays(5))
                .completed("Not completed")
                .createdAt(Instant.now())
                .completedAt(null)
                .build();
    }

    public static TaskResponse createTaskResponse3(){
        return TaskResponse.builder()
                .id(32L)
                .title("Take out the trash")
                .priority(Priority.HIGH)
                .estimatedDuration(5)
                .difficultyLevel(DifficultyLevel.VERY_EASY)
                .deadline(LocalDate.now().plusDays(3))
                .completed("Not completed")
                .createdAt(Instant.now())
                .completedAt(null)
                .build();
    }

    public static Page<TaskResponse> createPage(){

        return new PageImpl<>(
                List.of(createTaskResponse(),
                        createTaskResponse2(),
                        createTaskResponse3()),
                PageRequest.of(0, 10), 2);
    }

    public static TaskResponse createCompletedTask() {
        return TaskResponse.builder()
                .id(12L)
                .title("Assignment")
                .description("Finish and submit the assignment before the due date")
                .priority(Priority.HIGH)
                .estimatedDuration(180)
                .difficultyLevel(DifficultyLevel.MEDIUM)
                .deadline(LocalDate.now().plusDays(5))
                .completed("Completed")
                .createdAt(Instant.now())
                .completedAt(Instant.now().plus(Duration.ofDays(5)))
                .build();
    }
}
