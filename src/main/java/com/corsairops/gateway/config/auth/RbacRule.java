package com.corsairops.gateway.config.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
public class RbacRule {
    private HttpMethod method;
    private String pathPattern;
    private List<String> roles;
}