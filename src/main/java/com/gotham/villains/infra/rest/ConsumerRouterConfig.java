package com.gotham.villains.infra.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ConsumerRouterConfig {

    public static final String API_BASE_PATH = "/reactive/api/v1";

    @Bean
    public RouterFunction<ServerResponse> villainRoutes(ConsumerController controller) {
        final String path = API_BASE_PATH + "/villains";
        return RouterFunctions.route()
                .GET(path + "/iAm", controller::iAm)
                .build();
    }

}
