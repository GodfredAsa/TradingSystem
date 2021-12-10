package com.example.demo.entities;

import com.example.demo.enums.AuthStatus;
import com.example.demo.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@JsonSerialize
public class QueueAuthenticationLog{
    private Long userID;
    private AuthStatus authStatus;
    private LocalDateTime localDateTime;
    private UserRole role;
}
