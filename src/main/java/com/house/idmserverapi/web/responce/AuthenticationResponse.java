package com.house.idmserverapi.web.responce;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthenticationResponse {

    String accessToken;
    String refreshToken;
    String code;

}
