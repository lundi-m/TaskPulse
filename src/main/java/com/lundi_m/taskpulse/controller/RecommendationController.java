package com.lundi_m.taskpulse.controller;

import com.lundi_m.taskpulse.dto.recommendation.RecommendationResponse;
import com.lundi_m.taskpulse.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping
    public ResponseEntity<RecommendationResponse> recommend(
            @AuthenticationPrincipal UserDetails userDetails
            ){

        RecommendationResponse response = recommendationService.recommend(userDetails.getUsername());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ranked")
    public ResponseEntity<List<RecommendationResponse>> rankAll(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        List<RecommendationResponse> responses = recommendationService.rankALl(userDetails.getUsername());

        return ResponseEntity.ok(responses);
    }
}
