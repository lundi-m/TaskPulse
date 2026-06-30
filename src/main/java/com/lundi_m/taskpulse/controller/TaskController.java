package com.lundi_m.taskpulse.controller;

import com.lundi_m.taskpulse.dto.TaskRequest;
import com.lundi_m.taskpulse.dto.TaskResponse;
import com.lundi_m.taskpulse.model.Task;
import com.lundi_m.taskpulse.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody TaskRequest request
            ){

        TaskResponse response = taskService.createTask(userDetails.getUsername(), request);

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getTasks(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String completed,
            @RequestParam(required = false) Integer priority,
            Pageable pageable
    ) {
        Page<TaskResponse> tasks = taskService.getTasks(userDetails.getUsername(), completed, priority, pageable);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id){

        TaskResponse task = taskService.getTaskResponseById(userDetails.getUsername(), id);

        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody TaskRequest request
    ){

        TaskResponse task = taskService.updateTask(userDetails.getUsername(), id, request);

        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> completeTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ){

        TaskResponse task = taskService.markComplete(userDetails.getUsername(), id);

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ){

        taskService.deleteTask(userDetails.getUsername(), id);

        return ResponseEntity.noContent().build();
    }
}