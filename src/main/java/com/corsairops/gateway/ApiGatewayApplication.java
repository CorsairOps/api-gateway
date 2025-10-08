package com.corsairops.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @GetMapping("/token")
    public Jwt getToken(Authentication authentication) {
        return (Jwt) authentication.getPrincipal();
    }

    @GetMapping("/authentication")
    public Authentication getAuthentication(Authentication authentication) {
        return authentication;
    }

}