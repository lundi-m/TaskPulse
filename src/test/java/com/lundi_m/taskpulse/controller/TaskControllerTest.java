package com.lundi_m.taskpulse.controller;

import com.lundi_m.taskpulse.dto.task.TaskRequest;
import com.lundi_m.taskpulse.dto.task.TaskResponse;
import com.lundi_m.taskpulse.exception.TaskNotFoundException;
import com.lundi_m.taskpulse.model.enums.Priority;
import com.lundi_m.taskpulse.security.JwtAuthenticationFilter;
import com.lundi_m.taskpulse.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static com.lundi_m.taskpulse.testUtil.TaskData.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private String toJson(Object object){
        return objectMapper.writeValueAsString(object);
    }

    @Test
    @WithMockUser
    void shouldCreateTask() throws Exception{

        when(taskService.createTask(anyString(), any(TaskRequest.class)))
                .thenReturn(createTaskResponse());

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(createTaskRequest())))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestForInvalidTaskRequest() throws Exception{

        TaskRequest request = new TaskRequest();

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturnAllTasksWhenNoFiltersProvided() throws Exception{

        when(taskService.getTasks(anyString(),
                any(),
                any(),
                any(Pageable.class)))
                .thenReturn(createPage());

        mockMvc.perform(get("/tasks")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].title").isNotEmpty())
                .andExpect(jsonPath("$.totalPages").value(1));

        verify(taskService).getTasks(
                anyString(),
                isNull(),
                isNull(),
                any(Pageable.class)
        );
    }

    @Test
    @WithMockUser
    void shouldFilterTasksByCompletionStatus() throws Exception{

        when(taskService.getTasks(anyString(),
                any(),
                any(),
                any(Pageable.class)))
                .thenReturn(createPage());

        mockMvc.perform(get("/tasks")
                        .param("completed" ,"Not completed")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(taskService).getTasks(
                anyString(),
                eq("Not completed"),
                isNull(),
                any(Pageable.class)
        );
    }

    @Test
    @WithMockUser
    void shouldFilterTasksByPriority() throws Exception{

        when(taskService.getTasks(anyString(),
                any(),
                any(),
                any(Pageable.class)))
                .thenReturn(createPage());

        mockMvc.perform(get("/tasks")
                        .param("priority", "HIGH")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].priority").value("HIGH"));

        verify(taskService).getTasks(
                anyString(),
                isNull(),
                eq(Priority.HIGH),
                any(Pageable.class)
        );
    }

    @Test
    @WithMockUser
    void shouldFilterTasksByCompletionStatusAndPriority() throws Exception{

        when(taskService.getTasks(anyString(),
                any(),
                any(),
                any(Pageable.class)))
                .thenReturn(createPage());

        mockMvc.perform(get("/tasks")
                        .param("completed", "Not completed")
                        .param("priority", "HIGH")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(taskService).getTasks(
                anyString(),
                eq("Not completed"),
                eq(Priority.HIGH),
                any(Pageable.class)
        );
    }

    @Test
    @WithMockUser
    void shouldReturnEmptyTaskPage() throws Exception{

        when(taskService.getTasks(
                anyString(),
                any(),
                any(),
                any(Pageable.class)
        )).thenReturn(Page.empty());

        mockMvc.perform(get("/tasks")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    @WithMockUser
    void shouldReturnTaskById() throws Exception{

        TaskResponse response = createTaskResponse();

        when(taskService.getTaskResponseById(anyString(), anyLong()))
                .thenReturn(response);

        mockMvc.perform(get("/tasks/" + response.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(response.getTitle()));
    }

    @Test
    @WithMockUser
    void shouldReturnNotFoundForWhenTaskDoesNotExist() throws Exception{

        when(taskService.getTaskResponseById(anyString(), anyLong()))
                .thenThrow(new TaskNotFoundException(99999L));

        mockMvc.perform(get("/tasks/" + 99999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldUpdateTask() throws Exception{

        TaskResponse response = createTaskResponse2();

        when(taskService.updateTask(anyString(), any(), any(TaskRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/tasks/" + response.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createTaskRequest())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldMarkTaskAsComplete() throws Exception{

        TaskResponse response = createCompletedTask();

        when(taskService.markComplete(anyString(), anyLong()))
                .thenReturn(response);

        mockMvc.perform(patch("/tasks/" + response.getId() + "/complete"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldDeleteTask() throws Exception{
        doNothing().when(taskService)
                .deleteTask(anyString(), anyLong());

        mockMvc.perform(delete("/tasks/" + createTaskResponse().getId()))
                .andExpect(status().isNoContent());
    }
}
