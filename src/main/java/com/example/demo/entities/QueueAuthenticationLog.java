package com.example.demo.entities;

import com.example.demo.enums.AuthStatus;
import com.example.demo.enums.UserRole;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
public class QueueAuthenticationLog implements Serializable {
    private Long userID;
    private AuthStatus authStatus;
    private LocalDateTime localDateTime;
    private UserRole role;
}
