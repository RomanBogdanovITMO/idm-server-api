package com.house.idmserverapi.web;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Endpoints {

    public static final String API_V1_PREFIX = "/api/v1/auth";
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String REFRESH_TOKEN = "/refresh";
    public static final String VALIDATION = "/validation";
    public static final String LOGOUT_PATH = API_V1_PREFIX + LOGOUT;
    public static final String REGISTER_PATH = API_V1_PREFIX + REGISTER;
    public static final String LOGIN_PATH = API_V1_PREFIX + LOGIN;
    public static final String REFRESH_TOKEN_PATH = API_V1_PREFIX + REFRESH_TOKEN;
    public static final String VALIDATION_PATH = API_V1_PREFIX + VALIDATION;

}
