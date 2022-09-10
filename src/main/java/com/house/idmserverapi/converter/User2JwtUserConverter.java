package com.house.idmserverapi.converter;

import com.house.idmserverapi.entity.AppUser;
import com.house.idmserverapi.entity.enums.Status;
import com.house.idmserverapi.security.jwt.JwtUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.Assert.notNull;

@Component
public class User2JwtUserConverter implements Converter<AppUser, JwtUser> {

    @Override
    public JwtUser convert(final AppUser source) {
        notNull(source, "User is null");

        return JwtUser.builder()
                .id(source.getId())
                .email(source.getEmail())
                .firstName(source.getFirstName())
                .nickName(source.getNickName())
                .password(source.getPassword())
                .status(source.getStatus())
                .authorities(mapToGrantedAuthorities(List.of(source.getStatus())))
                .build();
    }

    private List<GrantedAuthority> mapToGrantedAuthorities(final List<Status> userStatus) {
        return userStatus.stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.name())
                ).collect(Collectors.toUnmodifiableList());
    }
}
