package com.lundi_m.taskpulse.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefreshRequest {

    @NotNull(message = "Refresh token is required")
    String refreshToken;
}
