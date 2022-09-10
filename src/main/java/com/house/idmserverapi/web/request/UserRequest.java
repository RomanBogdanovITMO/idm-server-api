package com.house.idmserverapi.web.request;

import lombok.Data;

@Data
public class UserRequest {

    private String email;
    private String password;
    private String firstName;
    private String nickName;


}
