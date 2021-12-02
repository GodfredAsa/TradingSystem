package com.clientService.user.model;

import com.clientService.enums.AuthStatus;
import com.clientService.enums.UserRole;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class AuthenticationLog {
    private Long userID;
    private AuthStatus authStatus;
    private LocalDateTime localDateTime;
    private UserRole role;
}
