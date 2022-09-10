package com.house.idmserverapi.converter;

import com.house.idmserverapi.entity.AppUser;
import com.house.idmserverapi.entity.dto.UserDto;
import com.house.idmserverapi.entity.enums.Status;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.notNull;

@Component
public class UserDto2UserConverter implements Converter<UserDto, AppUser> {

    @Override
    public AppUser convert(@Nullable final UserDto source) {
        notNull(source, "UserDto must be set");
        hasText(source.getEmail(), "UserDto email must be set");

        return AppUser.builder()
                .firstName(source.getName())
                .email(source.getEmail())
                .nickName(source.getName() + "-" + source.getLocale())
                .status(Status.ACTIVE)
                .build();
    }
}
