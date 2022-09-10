package com.house.idmserverapi.web;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Provides request router functionality
 */

@FunctionalInterface
public interface Router {

    /**
     * Starts routing
     * @return server response
     */

    RouterFunction<ServerResponse> route();

}
