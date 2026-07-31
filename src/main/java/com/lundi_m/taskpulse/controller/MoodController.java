package com.lundi_m.taskpulse.controller;

import com.lundi_m.taskpulse.dto.mood.MoodRequest;
import com.lundi_m.taskpulse.dto.mood.MoodResponse;
import com.lundi_m.taskpulse.service.MoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mood")
@Tag(
        name = "Mood Tracking",
        description = "Operations for recording and retrieving user moods.")
public class MoodController {

    private final MoodService moodService;

    @PostMapping
    @Operation(
            summary = "Record mood",
            description = "Records a new mood entry for the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Mood recorded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid mood data")})
    public ResponseEntity<MoodResponse> logMood(
            @AuthenticationPrincipal UserDetails userDetails,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Mood details",
                    required = true)
            @Valid @RequestBody MoodRequest request
            ){

        MoodResponse response = moodService.logMood(userDetails.getUsername(), request);

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/current")
    @Operation(
            summary = "Get current mood",
            description = "Returns the user's most recently recorded mood.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mood found"),
            @ApiResponse(responseCode = "404", description = "No mood recorded")})
    public ResponseEntity<MoodResponse> getCurrentMood(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        MoodResponse response = moodService.getCurrentMood(userDetails.getUsername());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    @Operation(
            summary = "Get mood history",
            description = "Returns all previously recorded moods.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mood history retrieved")})
    public ResponseEntity<List<MoodResponse>> getMoodHistory(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        List<MoodResponse> response = moodService.getHistory(userDetails.getUsername());

        return ResponseEntity.ok(response);
    }
}
