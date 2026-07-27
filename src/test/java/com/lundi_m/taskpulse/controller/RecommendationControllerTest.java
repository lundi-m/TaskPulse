package com.lundi_m.taskpulse.controller;

import com.lundi_m.taskpulse.dto.recommendation.RecommendationResponse;
import com.lundi_m.taskpulse.exception.MoodNotFoundException;
import com.lundi_m.taskpulse.exception.NoIncompleteTasksFoundException;
import com.lundi_m.taskpulse.exception.RecommendationGenerationException;
import com.lundi_m.taskpulse.security.JwtAuthenticationFilter;
import com.lundi_m.taskpulse.service.RecommendationService;
import com.lundi_m.taskpulse.testUtil.RecommendationData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecommendationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecommendationService recommendationService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @WithMockUser
    void shouldReturnRecommendation() throws Exception{

        RecommendationResponse response = RecommendationData.createResponse();

        when(recommendationService.recommend(
                anyString()))
                .thenReturn(response);

        mockMvc.perform(get("/recommendations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(response.getTitle()))
                .andExpect(jsonPath("$.score").value(response.getScore()))
                .andExpect(jsonPath("$.reasoning").value(response.getReasoning()));
    }

    @Test
    @WithMockUser
    void shouldReturnRankedRecommendations() throws Exception{

        RecommendationResponse response = RecommendationData.createResponse();
        RecommendationResponse response2 = RecommendationData.createResponse2();

        when(recommendationService.rankALl(
                anyString()))
                .thenReturn(List.of(response, response2));

        mockMvc.perform(get("/recommendations/ranked"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].description").value(response.getDescription()))
                .andExpect(jsonPath("$[1].description").value(response2.getDescription()));
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenMoodNotFound() throws Exception{

        when(recommendationService.recommend(
                anyString()))
                .thenThrow(new MoodNotFoundException());

        mockMvc.perform(get("/recommendations"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenNoIncompleteTasksExists() throws Exception{

        when(recommendationService.rankALl(
                anyString()))
                .thenThrow(new NoIncompleteTasksFoundException());

        mockMvc.perform(get("/recommendations/ranked"))
                .andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser
    void shouldReturn422WhenRecommendationCannotBeGenerated() throws Exception{

        when(recommendationService.recommend(
                anyString()))
                .thenThrow(new RecommendationGenerationException());

        mockMvc.perform(get("/recommendations"))
                .andExpect(status().isUnprocessableContent());
    }
}
