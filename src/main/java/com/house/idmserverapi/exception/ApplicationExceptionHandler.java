package com.house.idmserverapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.house.idmserverapi.util.ApplicationConstants.*;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Slf4j
@Component
public class ApplicationExceptionHandler extends AbstractErrorWebExceptionHandler {

    public ApplicationExceptionHandler(final ErrorAttributes errorAttributes,
                                       final WebProperties.Resources resources,
                                       final ApplicationContext applicationContext,
                                       final ServerCodecConfigurer codecConfigurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageReaders(codecConfigurer.getReaders());
        this.setMessageWriters(codecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
        log.trace("Defining routing function");

        return RouterFunctions.route(RequestPredicates.all(), this::renderException);
    }

    private Mono<ServerResponse> renderException(final ServerRequest request) {
        log.trace("Rendering exception for request={}", request);

        final Map<String, Object> error = this.getErrorAttributes(request, ErrorAttributeOptions.defaults());
        final HttpStatus responseStatus = defineResponseStatus(error);
        error.remove(STATUS_ERROR_ATTRIBUTE);
        error.remove(REQUEST_ID_ERROR_ATTRIBUTE);
        error.remove(CLASS_ERROR_ATTRIBUTE);

        return ServerResponse
                .status(responseStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(error));
    }

    private HttpStatus defineResponseStatus(final Map<String, Object> error) {
        log.trace("Defining response status by error={}", error);

        final Class<?> clazz = (Class<?>) error.get(CLASS_ERROR_ATTRIBUTE);
        if (IllegalArgumentException.class.isAssignableFrom(clazz)) {
            return HttpStatus.BAD_REQUEST;
        }
        if (DataNotFoundException.class.isAssignableFrom(clazz)) {
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}

