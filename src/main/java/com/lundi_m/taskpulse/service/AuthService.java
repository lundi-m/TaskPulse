package com.lundi_m.taskpulse.service;

import com.lundi_m.taskpulse.dto.AuthResponse;
import com.lundi_m.taskpulse.dto.LoginRequest;
import com.lundi_m.taskpulse.dto.RegisterRequest;
import com.lundi_m.taskpulse.dto.UserResponse;
import com.lundi_m.taskpulse.model.TaskPulseUser;
import com.lundi_m.taskpulse.repository.UserRepository;
import com.lundi_m.taskpulse.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserResponse register(RegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        TaskPulseUser user = TaskPulseUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(LocalDate.now())
                .build();

        TaskPulseUser saved = userRepository.save(user);

        return UserResponse.builder()
                .id(saved.getId())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .email(saved.getEmail())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        TaskPulseUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken = jwtService.generateAccessToken(user.getEmail());

        return AuthResponse.builder()
                .token(accessToken)
                .build();
    }
}
