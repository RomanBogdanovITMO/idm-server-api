package com.house.idmserverapi.service.impl;

import com.house.idmserverapi.Repository.UserRepository;
import com.house.idmserverapi.entity.AppUser;
import com.house.idmserverapi.entity.enums.Status;
import com.house.idmserverapi.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.Objects;

import static org.springframework.util.Assert.notNull;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements DataService<AppUser, String> {

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public Flux<AppUser> getAll() {
        log.info("execute getAll user service....");

        return repository.findAll();
    }

    @Override
    public Mono<AppUser> findByParameter(final String email) {
        log.info("execute findByUserEmail user service: {}", email);

        return repository.findByEmail(email);
    }

    @Override
    public Mono<AppUser> register(final AppUser user) {
        notNull(user, "User must be set");
        log.info("in progress method register user service: {}", user);

        return repository.save(AppUser.builder()
                .email(user.getEmail())
                .nickName(user.getNickName())
                .firstName(user.getFirstName())
                .password(Objects.isNull(user.getPassword()) ? user.getPassword() : passwordEncoder.encode(user.getPassword()))
                .status(Objects.isNull(user.getStatus()) ? Status.ACTIVE : user.getStatus())
                .build());
    }

    @Override
    public Mono<AppUser> delete(final String email) {
        return null;
    }

}
