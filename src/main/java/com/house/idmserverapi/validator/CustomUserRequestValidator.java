package com.house.idmserverapi.validator;

import com.house.idmserverapi.web.request.UserRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

import static com.house.idmserverapi.util.ApplicationConstants.*;

public class CustomUserRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "field.required");

        UserRequest request = (UserRequest) target;
        if (request.getPassword() != null && request.getPassword().trim().length() < MINIMUM_PASS_LENGTH) {

            errors.reject("password", "The password must be at least [" + MINIMUM_PASS_LENGTH + "] characters in length.");

        } else if (request.getPassword() != null && request.getPassword().trim().length() > MAX_PASS_LENGTH) {

            errors.reject("password", "The password must be no more than [" + MAX_PASS_LENGTH + "] characters in length.");

        } else if (request.getEmail() != null && !patternMatches(request.getEmail(), REGULAR_EXPRESSION_VALID)) {

            errors.reject("email", "The email is not valid [" + request.getEmail() + "].");
        }
    }

    private boolean patternMatches(String emailAddress, String regexPattern) {

        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
