package com.lundi_m.taskpulse.controller;

import com.lundi_m.taskpulse.dto.mood.MoodRequest;
import com.lundi_m.taskpulse.exception.MoodNotFoundException;
import com.lundi_m.taskpulse.security.JwtAuthenticationFilter;
import com.lundi_m.taskpulse.service.MoodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;

import static com.lundi_m.taskpulse.testUtil.MoodData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MoodController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MoodService moodService;

    @MockitoBean
    private JwtAuthenticationFilter authenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser
    void shouldCreateMoodEntry() throws Exception{

        when(moodService.logMood(anyString(), any(MoodRequest.class)))
                .thenReturn(createMoodResponse());

        mockMvc.perform(post("/mood")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createMoodRequest())))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void ShouldReturnBadRequestForInvalidMoodRequest() throws Exception{

        MoodRequest request = new MoodRequest();

        mockMvc.perform(post("/mood")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldGetLatestMood() throws Exception{

        when(moodService.getCurrentMood(anyString()))
                .thenReturn(createMoodResponse());

        mockMvc.perform(get("/mood/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.moodType").exists())
                .andExpect(jsonPath("$.availableTime").isNumber());
    }

    @Test
    @WithMockUser
    void ShouldReturnNotFoundWhenMoodDoesNotExist() throws Exception{

        when(moodService.getCurrentMood(anyString()))
                .thenThrow(new MoodNotFoundException());

        mockMvc.perform(get("/mood/current"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturnMoodHistory() throws Exception{

        when(moodService.getHistory(anyString()))
                .thenReturn(java.util.List.of(createMoodResponse(), createMoodResponse2()));

        mockMvc.perform(get("/mood/history"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser
    void shouldReturnEmptyMoodHistory() throws Exception {

        when(moodService.getHistory(anyString()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/mood/history"))
                .andExpect(jsonPath("$").isEmpty());
    }
}
