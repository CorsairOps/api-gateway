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
            HttpServletRequestWrapper requestWrapper = createWrapper(request, userId);
            filterChain.doFilter(requestWrapper, response);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        }
    }

    private HttpServletRequestWrapper createWrapper(HttpServletRequest request, String userId) {
        return new HttpServletRequestWrapper(request) {
            @Override
            public String getHeader(String name) {
                return switch (name) {
                    case "X-User-Id" -> userId;
                    default -> super.getHeader(name);
                };
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                if (name.equals("X-User-Id")) {
                    return java.util.Collections.enumeration(java.util.List.of(getHeader(name)));
                }
                return super.getHeaders(name);
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                java.util.List<String> headerNames = java.util.Collections.list(super.getHeaderNames());
                headerNames.add("X-User-Id");
                return java.util.Collections.enumeration(headerNames);
            }
        };
    }

}