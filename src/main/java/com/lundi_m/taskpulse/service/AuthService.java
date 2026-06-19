package com.lundi_m.taskpulse.service;

import com.lundi_m.taskpulse.dto.RegisterRequest;
import com.lundi_m.taskpulse.dto.UserResponse;
import com.lundi_m.taskpulse.model.TaskPulseUser;
import com.lundi_m.taskpulse.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
}
