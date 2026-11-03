package com.ecommerce.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Email address is already registered")
public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super(String.format("Failed to register account: The email '%s' is already in use.", email));
    }
}