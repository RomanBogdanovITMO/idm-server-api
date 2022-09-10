package com.house.idmserverapi.service.impl;

import com.house.idmserverapi.security.jwt.JwtProperties;
import com.house.idmserverapi.security.jwt.JwtTokenProvider;
import com.house.idmserverapi.service.AuthService;
import com.house.idmserverapi.web.request.AuthenticationRequest;
import com.house.idmserverapi.web.request.TokenRequest;
import com.house.idmserverapi.web.responce.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtProperties properties;
    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Override
    public Mono<AuthenticationResponse> authentication(final AuthenticationRequest authRequest) {
        log.info("authentication process: {}", authRequest);

        return Mono.just(authRequest).flatMap(request -> authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()))
                .map(tokenProvider::createToken))
                .map(jwt -> AuthenticationResponse.builder()
                        .accessToken(jwt.get(properties.getNameAccessToken()))
                        .refreshToken(jwt.get(properties.getNameRefreshToken()))
                        .code(HttpStatus.OK.getReasonPhrase())
                        .build()
                );
    }

    @Override
    public Mono<AuthenticationResponse> refreshToken(final TokenRequest refresh) {
        log.info("refreshToken process: {}", refresh);

        return Mono.just(refresh).flatMap(refreshTokenRequest -> tokenProvider
                .refreshToken(refreshTokenRequest.getToken())
                .map(value -> AuthenticationResponse.builder()
                        .accessToken(value.get(properties.getNameAccessToken()))
                        .refreshToken(value.get(properties.getNameRefreshToken()))
                        .code(HttpStatus.OK.getReasonPhrase())
                        .build()));
    }

    @Override
    public Mono<AuthenticationResponse> validateToken(final TokenRequest token) {
        log.info("validationToken process: {}", token);

        return Mono.just(token).flatMap(tokenRequest -> tokenProvider
                .validationTokenGoogle(tokenRequest.getToken())
                .map(valueMap -> AuthenticationResponse.builder()
                        .accessToken(valueMap.get(properties.getNameAccessToken()))
                        .refreshToken(valueMap.get(properties.getNameRefreshToken()))
                        .code(valueMap.isEmpty() ? HttpStatus.BAD_REQUEST.getReasonPhrase() : HttpStatus.OK.getReasonPhrase())
                        .build()));
    }
}
