package com.corsairops.gateway.config.auth.rbac;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "rbac")
@Data
@NoArgsConstructor @AllArgsConstructor
public class RbacProperties {

    private List<RbacRoute> routes;

}