package com.house.idmserverapi.web.router;

import com.house.idmserverapi.config.api.UserApiInfo;
import com.house.idmserverapi.web.AuthHandler;
import com.house.idmserverapi.web.Router;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.house.idmserverapi.web.Endpoints.*;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthRouter implements Router {

    private final AuthHandler handler;

    @Bean
    @Override
    @UserApiInfo
    public RouterFunction<ServerResponse> route() {
        log.info("Routing request...");

        return RouterFunctions
                .route()
                .POST(REGISTER_PATH, handler::register)
                .POST(LOGIN_PATH, handler::login)
                .POST(REFRESH_TOKEN_PATH, handler::refreshToken)
                .GET(LOGOUT_PATH, handler::logout)
                .POST(VALIDATION_PATH, handler::validation)
                .build();

    }
}
