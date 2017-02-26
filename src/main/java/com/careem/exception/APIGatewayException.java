package com.careem.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class APIGatewayException extends RuntimeException {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, Collection<String>> errors;

    public APIGatewayException(String message, Map<String, Collection<String>> errors) {
        super(message);
        this.errors = errors;
    }

    public APIGatewayException(Map<String, Collection<String>> errors) {
        super(formatMessage(errors));
        this.errors = errors;
    }


    private static String formatMessage(Map<String, Collection<String>> errors) {
        try {
            return String.format("validation failed on saving: %s", objectMapper.writeValueAsString(errors));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public APIGatewayException(String url, Exception e) {
        super(messageWithUrl(url), e);
        e.printStackTrace();
    }

    public APIGatewayException(String url, String message) {
        super(String.format("%s: %s", messageWithUrl(url), message));
    }

    private static String messageWithUrl(String url) {
        return String.format("Exception while processing url: %s", url);
    }

    public Map<String, Collection<String>> getErrors() {
        return errors;
    }
}

