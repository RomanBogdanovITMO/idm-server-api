package com.house.idmserverapi.web.request;

import lombok.Data;

@Data
public class RefreshTokenRequest {

    private String refreshToken;
}
