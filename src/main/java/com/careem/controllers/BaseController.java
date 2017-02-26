package com.careem.controllers;

import com.careem.commons.WithInTransaction;
import com.careem.exception.APIGatewayException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.NoResultException;
import java.util.HashMap;

@RequestMapping(produces = "application/json")
@WithInTransaction
class BaseController {
    @ExceptionHandler(NoResultException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public HashMap<String, Object> handleRecordNotFoundException(RuntimeException ex) {
        HashMap<String, Object> errorMap = new HashMap<String, Object>() {{
            put("error", "Not Found");
            put("status", HttpStatus.NOT_FOUND.value());
            put("message", ex.getMessage());
        }};
        return errorMap;
    }

    @ExceptionHandler(APIGatewayException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public HashMap<String, Object> handleBadGateway(RuntimeException ex) {
        HashMap<String, Object> errorMap = new HashMap<String, Object>() {{
            put("error", "Not Found");
            put("status", HttpStatus.NOT_FOUND.value());
            put("message", ex.getMessage());
        }};
        return errorMap;
    }

}

