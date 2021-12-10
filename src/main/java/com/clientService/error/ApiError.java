package com.clientService.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.*;

//@Setter
//@Getter
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class ApiError {
//
//    int status;
//    String message;
//    long timestamp;
//    String path;
//    Map<String, String> validationErrors;
//
//    /**
//     * model to hold a single error instance
//     *
//     * @param status
//     * @param message
//     * @param path
//     */
//    public ApiError(int status, String message, String path) {
//        this.status = status;
//        this.message = message;
//        this.path = path;
//        this.timestamp = new Date().getTime();
//    }
//}

@Getter
@Setter
public class ApiError {

    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ApiError(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }
}
