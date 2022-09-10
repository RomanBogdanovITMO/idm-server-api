package com.house.idmserverapi.Repository;

import com.house.idmserverapi.entity.AppUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;


public interface UserRepository extends ReactiveCrudRepository<AppUser, UUID> {

    Mono<AppUser> findByEmail(final String email);
}
