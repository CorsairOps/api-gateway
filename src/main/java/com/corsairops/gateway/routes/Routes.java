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

    @Bean
    public RouterFunction<ServerResponse> assetServiceApiRoutes() {
        return route("assetServiceApiRoute")
                .before(BeforeFilterFunctions.uri(assetServiceUri))
                .route(RequestPredicates.path("/api/assets/**"), HandlerFunctions.http())
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