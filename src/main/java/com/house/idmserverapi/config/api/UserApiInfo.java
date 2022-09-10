package com.house.idmserverapi.config.api;

import com.house.idmserverapi.web.request.UserRequest;
import com.house.idmserverapi.web.responce.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.house.idmserverapi.web.Endpoints.REGISTER_PATH;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@RouterOperations({
        @RouterOperation(
                method = RequestMethod.POST,
                path = REGISTER_PATH,
                operation = @Operation(
                        description = "registration the user",
                        operationId = "registrationUser",
                        tags = "user",
                        requestBody = @RequestBody(
                                description = "User registration",
                                required = true,
                                content = @Content(schema = @Schema(implementation = UserRequest.class,
                                requiredProperties = {"description"}))),
                        responses = {
                                @ApiResponse(
                                        responseCode = "200",
                                        description = "Save the user",
                                        content = {
                                                @Content(
                                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                        schema = @Schema(implementation = UserResponse.class)

                                                )
                                        }
                                )
                        }
                )
        )
})
public @interface UserApiInfo {
}
