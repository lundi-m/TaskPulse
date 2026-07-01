package com.lundi_m.taskpulse.controller;

import com.lundi_m.taskpulse.dto.MoodRequest;
import com.lundi_m.taskpulse.dto.MoodResponse;
import com.lundi_m.taskpulse.service.MoodService;
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
public class MoodController {

    private final MoodService moodService;

    @PostMapping
    public ResponseEntity<MoodResponse> logMood(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody MoodRequest request
            ){

        MoodResponse response = moodService.logMood(userDetails.getUsername(), request);

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/current")
    public ResponseEntity<MoodResponse> getCurrentMood(
            @AuthenticationPrincipal UserDetails userDetails
    ){

        MoodResponse response = moodService.getCurrentMood(userDetails.getUsername());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<MoodResponse>> getMoodHistory(
            @AuthenticationPrincipal UserDetails userDetails
    ){
        List<MoodResponse> response = moodService.getHistory(userDetails.getUsername());

        return ResponseEntity.ok(response);
    }
}
