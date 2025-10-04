package com.corsairops.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
@RestController
public class CorsairopsApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CorsairopsApiGatewayApplication.class, args);
    }

    @GetMapping("/token")
    public Jwt getToken(@AuthenticationPrincipal Jwt jwt) {
        return jwt;
    }
    
    @GetMapping("/authentication")
    public Authentication getAuthentication(Authentication authentication) {
        return authentication;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin-only")
    public Map<String, String> adminOnly() {
        return Map.of("message", "Hello, Admin!");
    }

}