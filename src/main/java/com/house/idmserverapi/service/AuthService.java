package com.house.idmserverapi.service;

import com.house.idmserverapi.web.request.AuthenticationRequest;
import com.house.idmserverapi.web.request.TokenRequest;
import com.house.idmserverapi.web.responce.AuthenticationResponse;
import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<AuthenticationResponse> authentication(AuthenticationRequest authRequest);

    Mono<AuthenticationResponse> refreshToken(TokenRequest refresh);

    Mono<AuthenticationResponse> validateToken(TokenRequest refresh);

}
