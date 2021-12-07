package com.clientService.securityConfig;

import java.util.regex.Pattern;

public class EmailValidation {
    public static boolean patternMatches(String emailAddress) {
        String regexPattern = "^(.+)@(\\S+)$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
