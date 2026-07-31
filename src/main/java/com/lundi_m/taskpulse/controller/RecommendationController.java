package com.lundi_m.taskpulse.controller;

import com.lundi_m.taskpulse.dto.recommendation.RecommendationResponse;
import com.lundi_m.taskpulse.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendations")
@Tag(
        name = "Recommendations",
        description = "Mood-based task recommendation endpoints.")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping
    @Operation(
            summary = "Recommend a task",
            description = "Returns the highest-scoring task recommendation based on the user's latest mood, energy level, available time, task urgency, and task difficulty.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recommendation generated successfully"),
            @ApiResponse(responseCode = "404", description = "No recommendation could be generated")})
    public ResponseEntity<RecommendationResponse> recommend(
            @AuthenticationPrincipal UserDetails userDetails
            ){

        RecommendationResponse response = recommendationService.recommend(userDetails.getUsername());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ranked")
    @Operation(
            summary = "Get ranked recommendations",
            description = "Returns all incomplete tasks ranked from most to least suitable according to the recommendation engine.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ranked recommendations retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No incomplete tasks available")})
    public ResponseEntity<List<RecommendationResponse>> rankAll(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        List<RecommendationResponse> responses = recommendationService.rankALl(userDetails.getUsername());

        return ResponseEntity.ok(responses);
    }
}
