package com.lundi_m.taskpulse.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskPulseOpenApi(){

        final String securityScheme = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("TaskPulse API")
                        .description("""
                                Mood-based task management API.
                                
                                Features:
                                * JWT Authentication
                                * Task Management
                                * Mood Tracking
                                * Recommendation Engine
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Keabetswe Mathabatha")
                                .email("keabetswelundim@gmail.com")))

                .addSecurityItem(new SecurityRequirement()
                        .addList(securityScheme))

                .components(new Components()
                        .addSecuritySchemes(securityScheme,
                        new SecurityScheme()
                                .name(securityScheme)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
