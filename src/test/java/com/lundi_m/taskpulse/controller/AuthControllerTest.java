package com.lundi_m.taskpulse.controller;

import com.lundi_m.taskpulse.dto.auth.LoginRequest;
import com.lundi_m.taskpulse.dto.auth.RefreshRequest;
import com.lundi_m.taskpulse.dto.auth.RegisterRequest;
import com.lundi_m.taskpulse.exception.EmailAlreadyExistsException;
import com.lundi_m.taskpulse.exception.InvalidTokenException;
import com.lundi_m.taskpulse.security.JwtAuthenticationFilter;
import com.lundi_m.taskpulse.service.AuthService;
import com.lundi_m.taskpulse.testUtil.AuthData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static com.lundi_m.taskpulse.testUtil.AuthData.*;
import static com.lundi_m.taskpulse.testUtil.AuthData.createUserResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @MockitoBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private String toJson(Object object){
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void shouldRegisterUser() throws Exception{

        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(createUserResponse());

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(createRegisterRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    void shouldReturnBadRequestForInvalidRegisterRequest() throws Exception{

        RegisterRequest request = new RegisterRequest();

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception{

        when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new EmailAlreadyExistsException("Email already exist"));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(createUserResponse())))
                .andExpect(status().isConflict());
    }

    @Test
    void ShouldLoginSuccessfully() throws Exception{

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(AuthData.createAuthResponse());

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(createLoginRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void shouldReturnBadRequestForInvalidLoginRequest() throws Exception{

        LoginRequest request = new LoginRequest();

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUnAuthorizedForBadCredentials() throws Exception{

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new BadCredentialsException("Invalid Credentials"));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(createLoginRequest())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRefreshToken() throws Exception{

        when(authService.refresh(any(RefreshRequest.class)))
                .thenReturn(AuthData.createAuthResponse());

        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(createRefreshRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void shouldReturnUnauthorizedWhenRefreshTokenIsInvalid() throws Exception{

        RefreshRequest request = new RefreshRequest();

        when(authService.refresh(any(RefreshRequest.class)))
                .thenThrow(new InvalidTokenException("Invalid Token"));

        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldLogoutSuccessfully() throws Exception{

        doNothing().when(authService).logout(AuthData.createRefreshRequest());

        mockMvc.perform(post("/api/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(createRefreshRequest())))
                .andExpect(status().isOk());
    }
}
