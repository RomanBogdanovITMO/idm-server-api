package com.house.idmserverapi.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {


    @JsonProperty("given_name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("email_verified")
    private Boolean emailVerified;

    @JsonProperty("locale")
    private String locale;

}
