package com.house.idmserverapi.web.request;

import lombok.Data;



@Data
public class AuthenticationRequest {

    private String email;
    private String password;
}
