package com.house.idmserverapi.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.Map;

import static com.house.idmserverapi.util.ApplicationConstants.*;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        final Map<String, Object> errorMap = new HashMap<>();
        final Throwable error = getError(request);
        errorMap.put(MESSAGE_ERROR_ATTRIBUTE, error.getMessage());
        errorMap.put(URL_ERROR_ATTRIBUTE, request.path());
        errorMap.put(CLASS_ERROR_ATTRIBUTE, error.getClass());

        return errorMap;
    }
}


