package com.lundi_m.taskpulse.service;

import com.lundi_m.taskpulse.dto.*;
import com.lundi_m.taskpulse.model.RefreshToken;
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
    private final RefreshTokenService refreshTokenService;


    public UserResponse register(RegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        TaskPulseUser user = TaskPulseUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        TaskPulseUser saved = userRepository.save(user);

        return UserResponse.builder()
                .id(saved.getId())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .email(saved.getEmail())
                .build();
    }

    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        TaskPulseUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public AuthResponse refresh(RefreshRequest request){
        RefreshToken refreshToken = refreshTokenService.verifyAndGet(request.getRefreshToken());
        String newAccessToken = jwtService.generateAccessToken(refreshToken.getUser().getEmail());

        return AuthResponse.builder()
                .refreshToken(refreshToken.getToken())
                .accessToken(newAccessToken)
                .build();
    }

    public void logout(RefreshRequest request){
        refreshTokenService.deleteByToken(request.getRefreshToken());
    }
}
