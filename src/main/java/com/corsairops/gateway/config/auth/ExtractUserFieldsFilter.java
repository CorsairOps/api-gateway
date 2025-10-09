package com.corsairops.gateway.config.auth;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.stream.Collectors;

public class ExtractUserFieldsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
                filterChain.doFilter(request, response);
                return;
            }
            String userId = authentication.getName();
            String roles = authentication.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                    .collect(Collectors.joining(","));
            HttpServletRequestWrapper requestWrapper = createWrapper(request, userId, roles);
            filterChain.doFilter(requestWrapper, response);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        }
    }

    private HttpServletRequestWrapper createWrapper(HttpServletRequest request, String userId, String roles) {
        return new HttpServletRequestWrapper(request) {
            @Override
            public String getHeader(String name) {
                return switch (name) {
                    case "X-User-Id" -> userId;
                    case "X-User-Roles" -> roles;
                    default -> super.getHeader(name);
                };
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                if (name.equals("X-User-Id") || name.equals("X-User-Roles")) {
                    return java.util.Collections.enumeration(java.util.List.of(getHeader(name)));
                }
                return super.getHeaders(name);
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                java.util.List<String> headerNames = java.util.Collections.list(super.getHeaderNames());
                headerNames.add("X-User-Id");
                headerNames.add("X-User-Roles");
                return java.util.Collections.enumeration(headerNames);
            }
        };
    }

}