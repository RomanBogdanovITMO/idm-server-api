package com.house.idmserverapi.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface DataService<T, E> {

    Flux<T> getAll();

    Mono<T> findByParameter(E value);

    Mono<T> register(T source);

    Mono<T> delete(E value);

}


