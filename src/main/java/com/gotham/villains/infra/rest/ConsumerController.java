package com.gotham.villains.infra.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Controller
public class ConsumerController {

    public Mono<ServerResponse> iAm(ServerRequest serverRequest){
        return serverRequest.bodyToMono(Void.class)
                .thenReturn("I am the batCave consumer")
                .flatMap(ok()::bodyValue);
    }
}
