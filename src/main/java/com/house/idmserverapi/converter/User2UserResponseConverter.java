package com.house.idmserverapi.converter;

import com.house.idmserverapi.entity.AppUser;
import com.house.idmserverapi.web.responce.UserResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

import static org.springframework.util.Assert.notNull;

@Component
public class User2UserResponseConverter implements Converter<AppUser, UserResponse> {


    @Override
    public UserResponse convert(@NotNull final AppUser source) {
        notNull(source, "User is null");

        return UserResponse.builder()
                .login(source.getEmail())
                .nickName(source.getNickName())
                .build();
    }
}
