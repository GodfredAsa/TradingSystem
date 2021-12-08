package com.clientService.user.service;

import com.clientService.enums.AuthStatus;
import com.clientService.loggerPack.LoggerConfig;
import com.clientService.securityConfig.JWTUtility;
import com.clientService.securityConfig.SendLoggerRequest;
import com.clientService.user.model.AppUser;
import com.clientService.user.model.HttpResponseBody;
import com.clientService.user.model.JwtRequest;
import com.clientService.user.repository.AppUserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AppUserSignInService {
    private final AuthenticationManager authenticationManager;
    private final AppUserAuthService appUserAuthService;
    private final JWTUtility jwtUtility;
    private final HttpResponseBody response;
    private final AppUserRepository appUserRepository;
    private  final RestTemplate restTemplate;

    @Value("${report.url}")
    private String reportUrl;

    public AppUserSignInService(AuthenticationManager authenticationManager,
                                AppUserAuthService appUserAuthService, JWTUtility jwtUtility,
                                HttpResponseBody response, AppUserRepository appUserRepository,
                                RestTemplate restTemplate) {
        this.authenticationManager = authenticationManager;
        this.appUserAuthService = appUserAuthService;
        this.jwtUtility = jwtUtility;
        this.response = response;
        this.appUserRepository = appUserRepository;
        this.restTemplate = restTemplate;
    }


    public ResponseEntity<?> authenticateUser(JwtRequest jwtRequest){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getEmail(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            response.setMessage("Email or Password incorrect");
            LoggerConfig.LOGGER.error(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        final UserDetails userDetails = appUserAuthService.loadUserByUsername(jwtRequest.getEmail());

        final String token =
                jwtUtility.generateToken(userDetails);
        AppUser user = appUserRepository.getAppUserByEmail(userDetails.getUsername());
        JSONObject log = new JSONObject();
        log.put("userID", user.getId());
        log.put("authStatus", AuthStatus.LOGIN);
        log.put("role", user.getUserRole());
        log.put("localDateTime", LocalDateTime.now());

        HttpEntity<String> request = SendLoggerRequest.sendLoggerRequest(log);
//        restTemplate.postForObject(reportUrl+"userAuthentication", request ,String.class);

        Map<String, Object> responseBody =  new HashMap<>();
        user.setId(0L);
        user.setPassword("");
        responseBody.put("token", token);
        responseBody.put("user", user);

        response.setMessage(responseBody);
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

}
