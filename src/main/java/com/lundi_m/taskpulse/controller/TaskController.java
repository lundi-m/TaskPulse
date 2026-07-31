package com.lundi_m.taskpulse.controller;

import com.lundi_m.taskpulse.dto.task.TaskRequest;
import com.lundi_m.taskpulse.dto.task.TaskResponse;
import com.lundi_m.taskpulse.model.enums.Priority;
import com.lundi_m.taskpulse.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Task Management", description = "Operations for managing user tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(
            summary = "Create task",
            description = "Creates a new task for the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid task data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    public ResponseEntity<TaskResponse> createTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Task information",
                    required = true)
            @Valid @RequestBody TaskRequest request
            ){

        TaskResponse response = taskService.createTask(userDetails.getUsername(), request);

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    @Operation(
            summary = "Get tasks",
            description = "Returns a paginated list of tasks with optional completion status and priority filters.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    public ResponseEntity<Page<TaskResponse>> getTasks(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(
                    description = "Completion status",
                    example = "Not completed")
            @RequestParam(required = false) String completed,
            @Parameter(
                    description = "Task priority",
                    example = "HIGH")
            @RequestParam(required = false) Priority priority,
            Pageable pageable
    ) {
        Page<TaskResponse> tasks = taskService.getTasks(userDetails.getUsername(), completed, priority, pageable);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get task by ID",
            description = "Returns a specific task owned by the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found")})
    public ResponseEntity<TaskResponse> getTaskById(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(
                    description = "Task ID",
                    example = "1")
            @PathVariable Long id){

        TaskResponse task = taskService.getTaskResponseById(userDetails.getUsername(), id);

        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update task",
            description = "Updates an existing task.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Task not found")})
    public ResponseEntity<TaskResponse> updateTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated task information",
                    required = true)
            @Valid @RequestBody TaskRequest request
    ){

        TaskResponse task = taskService.updateTask(userDetails.getUsername(), id, request);

        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}/complete")
    @Operation(
            summary = "Mark task as completed",
            description = "Marks a task as completed.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task marked as completed"),
            @ApiResponse(responseCode = "404", description = "Task not found")})
    public ResponseEntity<TaskResponse> completeTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ){

        TaskResponse task = taskService.markComplete(userDetails.getUsername(), id);

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete task",
            description = "Deletes a task owned by the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")})
    public ResponseEntity<Void> deleteTask(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id
    ){

        taskService.deleteTask(userDetails.getUsername(), id);

        return ResponseEntity.noContent().build();
    }
}