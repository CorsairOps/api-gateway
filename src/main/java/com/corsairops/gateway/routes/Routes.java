package com.corsairops.gateway.routes;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class Routes {

    @Value("${servers.asset-service.uri}")
    private String assetServiceUri;

    @Value("${servers.user-service.uri}")
    private String userServiceUri;

    @Value("${servers.mission-service.uri}")
    private String missionServiceUri;

    @Bean
    public RouterFunction<ServerResponse> assetServiceApiRoutes() {
        return route("assetServiceApiRoute")
                .before(BeforeFilterFunctions.uri(assetServiceUri))
                .route(RequestPredicates.path("/api/assets/**"), HandlerFunctions.http())
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> assetServiceSwaggerRoute() {
        return route("assetServiceSwaggerRoute")
                .before(BeforeFilterFunctions.uri(assetServiceUri))
                .before(BeforeFilterFunctions.setPath("/api-docs"))
                .route(RequestPredicates.path("/aggregate/asset-service/api-docs"), HandlerFunctions.http())
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceApiRoutes() {
        return route("userServiceApiRoute")
                .before(BeforeFilterFunctions.uri(userServiceUri))
                .route(RequestPredicates.path("/api/users/**"), HandlerFunctions.http())
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceSwaggerRoute() {
        return route("userServiceSwaggerRoute")
                .before(BeforeFilterFunctions.uri(userServiceUri))
                .before(BeforeFilterFunctions.setPath("/api-docs"))
                .route(RequestPredicates.path("/aggregate/user-service/api-docs"), HandlerFunctions.http())
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> missionServiceApiRoutes() {
        return route("missionServiceApiRoute")
                .before(BeforeFilterFunctions.uri(missionServiceUri))
                .route(RequestPredicates.path("/api/missions/**"), HandlerFunctions.http())
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> missionServiceSwaggerRoute() {
        return route("missionServiceSwaggerRoute")
                .before(BeforeFilterFunctions.uri(missionServiceUri))
                .before(BeforeFilterFunctions.setPath("/api-docs"))
                .route(RequestPredicates.path("/aggregate/mission-service/api-docs"), HandlerFunctions.http())
                .build();
    }


    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return route("fallbackRoute")
                .GET("/fallbackRoute", request -> ServerResponse
                        .status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service is currently unavailable. Please try again later.")
                )
                .build();
    }
}