package com.clientService.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    int status;
    String message;
    long timestamp;
    String path;
    Map<String, String> validationErrors;

    /**
     *  model to hold a single error instance
     * @param status
     * @param message
     * @param path
     */
    public ApiError(int status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.timestamp = new Date().getTime();
    }
}
