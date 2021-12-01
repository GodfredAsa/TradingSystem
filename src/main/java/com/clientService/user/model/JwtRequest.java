package com.clientService.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest {
    /**
     * JwtRequest Model acting as SignIn Form
     */
    private String email;
    private String password;
}
