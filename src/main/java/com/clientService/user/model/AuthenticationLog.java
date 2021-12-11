package com.clientService.user.model;

import com.clientService.enums.AuthStatus;
import com.clientService.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class AuthenticationLog implements Serializable {
    private Long userID;
    private AuthStatus authStatus;
    private LocalDateTime localDateTime;
    private UserRole role;
}
