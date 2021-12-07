package com.clientService.user.model;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class HttpResponseBody implements Serializable {
    private Object message;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}