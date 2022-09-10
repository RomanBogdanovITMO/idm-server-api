package com.house.idmserverapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.util")
public class AppProperties {

    @NotBlank
    private String domainNameFront;
}
