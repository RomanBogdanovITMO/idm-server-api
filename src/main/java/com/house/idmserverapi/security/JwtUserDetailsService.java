package com.house.idmserverapi.security;

import com.house.idmserverapi.converter.User2JwtUserConverter;
import com.house.idmserverapi.entity.AppUser;
import com.house.idmserverapi.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@Slf4j
@RequiredArgsConstructor
public class JwtUserDetailsService implements ReactiveUserDetailsService {

    private final DataService<AppUser, String> userService;
    private final User2JwtUserConverter converter;


    @Override
    public Mono<UserDetails> findByUsername(final String userNameOfEmail) {
        return userService.findByParameter(userNameOfEmail)
                .map(converter::convert)
                .cast(UserDetails.class);
    }
}
