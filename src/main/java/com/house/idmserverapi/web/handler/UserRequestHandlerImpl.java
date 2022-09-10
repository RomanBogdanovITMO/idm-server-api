package com.house.idmserverapi.web.handler;

import com.house.idmserverapi.entity.AppUser;
import com.house.idmserverapi.service.AuthService;
import com.house.idmserverapi.service.DataService;
import com.house.idmserverapi.validator.CustomUserRequestValidator;
import com.house.idmserverapi.web.AuthHandler;
import com.house.idmserverapi.web.request.AuthenticationRequest;
import com.house.idmserverapi.web.request.TokenRequest;
import com.house.idmserverapi.web.request.UserRequest;
import com.house.idmserverapi.web.responce.AuthenticationResponse;
import com.house.idmserverapi.web.responce.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRequestHandlerImpl implements AuthHandler {

    private final DataService<AppUser, String> service;
    private final AuthService serviceAuth;
    private final Converter<UserRequest, AppUser> requestConverter;
    private final Converter<AppUser, UserResponse> responseConverter;

    @Override
    public Mono<ServerResponse> login(final ServerRequest request) {

        final Mono<AuthenticationResponse> out = request.bodyToMono(AuthenticationRequest.class)
                .filter(Objects::nonNull)
                .flatMap(serviceAuth::authentication)
                .doOnEach(item -> log.info(item.toString()));

        return ServerResponse
                .ok()
                .body(BodyInserters.fromProducer(out, AuthenticationResponse.class));

    }

    @Override
    public Mono<ServerResponse> register(final ServerRequest request) {
        
        Validator validator = new CustomUserRequestValidator();
        final Mono<UserResponse> out = request.bodyToMono(UserRequest.class)
                .map(body -> {
                    Errors errors = new BeanPropertyBindingResult(body, UserRequest.class.getName());
                    validator.validate(body, errors);
                    if (errors.getAllErrors().isEmpty()){
                       return requestConverter.convert(body);
                    } else {
                        throw new IllegalArgumentException(errors.getAllErrors().toString());
                    }
                })
                .filter(Objects::nonNull)
                .flatMap(service::register)
                .map(responseConverter::convert)
                .filter(Objects::nonNull)
                .doOnEach(item -> log.info(item.toString()));

        return ServerResponse
                .ok()
                .body(BodyInserters.fromProducer(out, UserResponse.class));
    }

    @Override
    public Mono<ServerResponse> refreshToken(final ServerRequest request) {
        final Mono<AuthenticationResponse> out = request.bodyToMono(TokenRequest.class)
                .filter(Objects::nonNull)
                .flatMap(serviceAuth::refreshToken)
                .doOnEach(item -> log.info(item.toString()));

        return ServerResponse
                .ok()
                .body(BodyInserters.fromProducer(out, AuthenticationResponse.class));
    }

    @Override
    public Mono<ServerResponse> logout(final ServerRequest request) {
        return ServerResponse
                .ok().build();
    }

    @Override
    public Mono<ServerResponse> validation(final ServerRequest request) {
        final Mono<AuthenticationResponse> out = request.bodyToMono(TokenRequest.class)
                .filter(Objects::nonNull)
                .flatMap(serviceAuth::validateToken)
                .doOnEach(item -> log.info(item.toString()));

        return ServerResponse
                .ok()
                .body(BodyInserters.fromProducer(out, AuthenticationResponse.class));
    }

}
