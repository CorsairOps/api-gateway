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
            UserFields userFields = extractUserFields(authentication);
            HttpServletRequestWrapper requestWrapper = createWrapper(request, userFields);
            filterChain.doFilter(requestWrapper, response);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
        }
    }

    private UserFields extractUserFields(Authentication authentication) {
        Jwt principal = (Jwt) authentication.getPrincipal();

        String role = authentication.getAuthorities().stream()
                .findFirst().map(Object::toString).orElse(null);

        return UserFields.builder()
                .id(principal.getClaimAsString("sub"))
                .email(principal.getClaimAsString("email"))
                .givenName(principal.getClaimAsString("given_name"))
                .familyName(principal.getClaimAsString("family_name"))
                .gender(principal.getClaimAsString("gender"))
                .role(role)
                .build();
    }

    private HttpServletRequestWrapper createWrapper(HttpServletRequest request, UserFields userFields) {

        return new HttpServletRequestWrapper(request) {
            @Override
            public String getHeader(String name) {
                return switch (name) {
                    case "X-User-Id" -> userFields.getId();
                    case "X-User-Email" -> userFields.getEmail();
                    case "X-User-GivenName" -> userFields.getGivenName();
                    case "X-User-FamilyName" -> userFields.getFamilyName();
                    case "X-User-Gender" -> userFields.getGender();
                    case "X-User-Role" -> userFields.getRole();
                    default -> super.getHeader(name);
                };
            }

            @Override
            public Enumeration<String> getHeaders(String name) {
                if (name.equals("X-User-Id") || name.equals("X-User-Email") || name.equals("X-User-GivenName") ||
                        name.equals("X-User-FamilyName") || name.equals("X-User-Gender") || name.equals("X-User-Role")) {
                    return java.util.Collections.enumeration(java.util.List.of(getHeader(name)));
                }
                return super.getHeaders(name);
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                java.util.List<String> headerNames = java.util.Collections.list(super.getHeaderNames());
                headerNames.add("X-User-Id");
                headerNames.add("X-User-Email");
                headerNames.add("X-User-GivenName");
                headerNames.add("X-User-FamilyName");
                headerNames.add("X-User-Gender");
                headerNames.add("X-User-Role");
                return java.util.Collections.enumeration(headerNames);
            }
        };
    }

}