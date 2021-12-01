package com.clientService.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    /**
     * JwtResponse Model for user Token
     */
    private String jwtToken;

}