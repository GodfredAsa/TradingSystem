package com.example.demo.entities;

import com.example.demo.enums.AuthStatus;
import com.example.demo.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@JsonSerialize
public class QueueAuthenticationLog implements Serializable {
    private long userID;
    private AuthStatus authStatus;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime localDateTime;
    private UserRole role;
}
