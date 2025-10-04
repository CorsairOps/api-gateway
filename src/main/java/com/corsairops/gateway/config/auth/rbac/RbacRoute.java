package com.corsairops.gateway.config.auth.rbac;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class RbacRoute {
    private String path;
    private String[] methods;
    private String[] roles;
}