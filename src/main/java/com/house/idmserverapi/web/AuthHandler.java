package com.house.idmserverapi.web;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Provider server request handling functionality
 */

public interface AuthHandler {

    Mono<ServerResponse> login(final ServerRequest request);

    Mono<ServerResponse> register(final ServerRequest request);

    Mono<ServerResponse> refreshToken(final ServerRequest request);

    Mono<ServerResponse> logout(final ServerRequest request);

    Mono<ServerResponse> validation(final ServerRequest request);

}
