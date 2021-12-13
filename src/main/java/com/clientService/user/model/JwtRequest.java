package com.clientService.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class JwtRequest {
    /**
     * JwtRequest Model acting as SignIn Form
     */
    private String email;
    private String password;
}
