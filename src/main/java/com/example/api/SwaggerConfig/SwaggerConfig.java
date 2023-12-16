package com.example.api.SwaggerConfig;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Clubs UIR API",
                version = "1.0",
                description = "API pour la gestion des clubs de l'UIR"
        ),
        servers = @Server(url = "http://localhost:8081", description = "Local server for development")
)
public class SwaggerConfig {}
