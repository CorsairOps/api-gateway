package com.corsairops.gateway.config.auth;

import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

public class RbacRules {

    public static final List<RbacRule> assetServiceRules = List.of(
            new RbacRule(HttpMethod.GET, "/api/assets/**", List.of("ADMIN", "PLANNER", "OPERATOR", "TECHNICIAN")),
            new RbacRule(HttpMethod.POST, "/api/assets/**", List.of("ADMIN", "PLANNER")),
            new RbacRule(HttpMethod.PUT, "/api/assets/**", List.of("ADMIN", "PLANNER")),
            new RbacRule(HttpMethod.DELETE, "/api/assets/**", List.of("ADMIN", "PLANNER"))
    );

    public static List<RbacRule> getAllRbacRules() {
        List<RbacRule> allRules = new ArrayList<>();
        allRules.addAll(assetServiceRules);
        return allRules;
    }

}