package com.lundi_m.taskpulse.controller;

import com.lundi_m.taskpulse.dto.TaskRequest;
import com.lundi_m.taskpulse.dto.TaskResponse;
import com.lundi_m.taskpulse.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
