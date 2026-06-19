package com.lundi_m.taskpulse.controller;

import com.lundi_m.taskpulse.dto.RegisterRequest;
import com.lundi_m.taskpulse.dto.UserResponse;
import com.lundi_m.taskpulse.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){
        UserResponse response = authService.register(request);

        return ResponseEntity.status(201).body(response);
    }
}
