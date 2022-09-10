package com.house.idmserverapi.converter;

import com.house.idmserverapi.entity.AppUser;
import com.house.idmserverapi.web.request.UserRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;


import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

@Component
public class UserRequest2UserConverter implements Converter<UserRequest, AppUser> {

    @Override
    public AppUser convert(@Nullable final UserRequest source) {
        notNull(source, "User must be set");
        hasText(source.getEmail(), "User email must be set");
        hasText(source.getPassword(), "User password must be set");
        hasText(source.getNickName(), "User nickName must be set");
        hasText(source.getFirstName(), "User first name must be set");

        return AppUser.builder()
                .email(source.getEmail())
                .password(source.getPassword())
                .firstName(source.getFirstName())
                .nickName(source.getNickName())
                .build();
    }
}
