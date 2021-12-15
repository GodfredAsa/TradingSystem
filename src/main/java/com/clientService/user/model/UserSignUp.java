package com.clientService.user.model;

import com.clientService.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserSignUp {

    /**
     * UserSignUp Model to act as User SignUp Form
     * with AllArgsConstructor
     */
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private UserRole userRole;

    public UserSignUp(String firstName, String lastName, String password, String email, UserRole userRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.userRole = userRole;
    }
}
