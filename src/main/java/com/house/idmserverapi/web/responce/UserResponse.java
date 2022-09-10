package com.house.idmserverapi.web.responce;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@Schema(description = "Data model for the user response")
public class UserResponse {

    @Schema(description = "login user", example = "q@mail.ru")
    String login;

    @Schema(description = "nickname user", example = "ivan-ivanov")
    String nickName;
}
