package com.corsairops.gateway.config.auth.rbac;

import com.corsairops.shared.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class RbacFilter extends OncePerRequestFilter {

    private final RbacProperties rbacProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String requestPath = request.getRequestURI();
        String requestMethod = request.getMethod();
        String userRole = getUserRole(authentication);

        if (isAccessAllowed(requestPath, requestMethod, userRole)) {
            filterChain.doFilter(request, response);
        } else {
            denyAccess(response);
        }
    }

    private String getUserRole(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities().isEmpty()) {
            return null;
        }
        String role = authentication.getAuthorities().stream()
                .findFirst().map(Object::toString).orElse(null);
        if (role == null || !role.startsWith("ROLE_")) {
            return null;
        }
        return role.substring(5); // Remove "ROLE_" prefix
    }

    private boolean isAccessAllowed(String path, String method, String role) {
        RbacRoute matchingRoute = findMatchingRouteByPathAndMethod(path, method);
        if (matchingRoute == null) {
            // No matching route means it is not protected, allow access
            return true;
        }
        return hasRequiredRole(matchingRoute, role);
    }

    private RbacRoute findMatchingRouteByPathAndMethod(String path, String method) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (RbacRoute route : rbacProperties.getRoutes()) {
            if (pathMatcher.match(route.getPath(), path)) {
                // If method is not specified in the route, it matches all methods
                if (route.getMethods().length == 0) {
                    return route;
                }
                // Check if the method matches
                for (String m : route.getMethods()) {
                    if (m.equalsIgnoreCase(method)) {
                        return route;
                    }
                }
            }
        }

        return null;
    }

    private boolean hasRequiredRole(RbacRoute route, String userRole) {
        if (route.getRoles().length == 0) {
            // No roles specified means open to all authenticated users
            return true;
        }
        for (String role : route.getRoles()) {
            if (role.equalsIgnoreCase(userRole)) {
                return true;
            }
        }
        return false;
    }

    private void denyAccess(HttpServletResponse response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ErrorResponse errorResponse = new ErrorResponse(403, "Forbidden", "You do not have permission to access this resource.", LocalDateTime.now().toString());
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getOutputStream().write(objectMapper.writeValueAsBytes(errorResponse));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}