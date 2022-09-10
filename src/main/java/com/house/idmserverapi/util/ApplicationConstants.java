package com.house.idmserverapi.util;

public interface ApplicationConstants {

    String MESSAGE_ERROR_ATTRIBUTE = "message";
    String URL_ERROR_ATTRIBUTE = "requested_url";
    String CLASS_ERROR_ATTRIBUTE = "class";
    String REQUEST_ID_ERROR_ATTRIBUTE = "requestId";
    String STATUS_ERROR_ATTRIBUTE = "status";
    String REGULAR_EXPRESSION_VALID = "^(.+)@(\\S+)$";
    Integer MINIMUM_PASS_LENGTH = 6;
    Integer MAX_PASS_LENGTH = 32;

}
