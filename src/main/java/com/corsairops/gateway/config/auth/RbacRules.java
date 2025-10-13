package com.corsairops.gateway.config.auth;

import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

public class RbacRules {

    /*
        Role Order is:
        ADMIN > PLANNER > OPERATOR > TECHNICIAN > ANALYST
        The role IOT_DEVICE also exists for specific endpoints.
     */


    private static final List<String> allRoles = List.of("ADMIN", "PLANNER", "OPERATOR", "TECHNICIAN", "ANALYST");
    private static final List<String> adminOnlyRoles = List.of("ADMIN");
    private static final List<String> plannerAndAboveRoles = List.of("ADMIN", "PLANNER");
    private static final List<String> operatorAndAboveRoles = List.of("ADMIN", "PLANNER", "OPERATOR");
    private static final List<String> technicianAndAboveRoles = List.of("ADMIN", "PLANNER", "OPERATOR", "TECHNICIAN");
    private static final List<String> analystAndAboveRoles = List.of("ADMIN", "PLANNER", "OPERATOR", "TECHNICIAN", "ANALYST");


    public static final List<RbacRule> assetServiceRules = List.of(
            new RbacRule(HttpMethod.GET, "/api/assets/**", allRoles),
            new RbacRule(HttpMethod.POST, "/api/assets/**", plannerAndAboveRoles),
            new RbacRule(HttpMethod.PUT, "/api/assets/*/locations", List.of("ADMIN", "PLANNER", "IOT_DEVICE")),
            new RbacRule(HttpMethod.PUT, "/api/assets/**", plannerAndAboveRoles),
            new RbacRule(HttpMethod.DELETE, "/api/assets/**", plannerAndAboveRoles)
    );

    // Note: Technicians cannot view or modify missions
    public static final List<RbacRule> missionServiceRules = List.of(
            new RbacRule(HttpMethod.GET, "/api/missions/**", List.of("ADMIN", "PLANNER", "OPERATOR", "ANALYST")),
            new RbacRule(HttpMethod.POST, "/api/missions/**", plannerAndAboveRoles),
            new RbacRule(HttpMethod.PUT, "/api/missions/**", plannerAndAboveRoles),
            new RbacRule(HttpMethod.DELETE, "/api/missions/**", plannerAndAboveRoles),
            new RbacRule(HttpMethod.POST, "/api/missions/*/logs/**", List.of("ADMIN", "PLANNER", "OPERATOR")),
            new RbacRule(HttpMethod.GET, "/api/missions/assigned-missions/missions/**", List.of("ADMIN", "PLANNER", "ANALYST"))
    );

    // Note: Technicians and operators cannot view users directly
    public static final List<RbacRule> userServiceRules = List.of(
            new RbacRule(HttpMethod.GET, "/api/users/auth", allRoles),
            new RbacRule(HttpMethod.GET, "/api/users/**", List.of("ADMIN", "PLANNER", "ANALYST"))
    );

    public static final List<RbacRule> eventServiceRules = List.of(
            new RbacRule(HttpMethod.POST, "/events/publish/asset-location-updates", List.of("ADMIN", "PLANNER", "IOT_DEVICE")),
            new RbacRule(HttpMethod.POST, "/events/publish/**", adminOnlyRoles)
    );

    public static List<RbacRule> getAllRbacRules() {
        List<RbacRule> allRules = new ArrayList<>();
        allRules.addAll(assetServiceRules);
        allRules.addAll(missionServiceRules);
        allRules.addAll(userServiceRules);
        allRules.addAll(eventServiceRules);
        return allRules;
    }

}