package com.house.idmserverapi.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@Configuration
@ConfigurationProperties(prefix = "app.util")
public class JwtProperties {

    @NotBlank
    private String secretKey;

    @NotNull
    private Long validityInMin;

    @NotNull
    private Long validityRefreshInMin;

    @NotBlank
    private String nameAccessToken;

    @NotBlank
    private String nameRefreshToken;
}
