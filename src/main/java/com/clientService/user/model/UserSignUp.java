package com.clientService.user.model;

import com.clientService.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSignUp {

    /**
     * UserSignUp Model to act as User SignUp Form
     * with AllArgsConstructor
     */
    private String firstName;
    private String lastName;
    private String password;
    private String email;
}
