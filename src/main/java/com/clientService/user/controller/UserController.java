package com.clientService.user.controller;

import com.clientService.enums.AuthStatus;
import com.clientService.securityConfig.SendLoggerRequest;
import com.clientService.user.model.*;
import com.clientService.user.repository.AppUserRepository;
import com.clientService.user.service.AppUserService;
import com.clientService.order.model.Order;

import com.clientService.securityConfig.JWTUtility;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/client/")
public class UserController {
    private final AppUserService appUserService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtility jwtUtility;
    private final RestTemplate restTemplate;
    private final AppUserRepository appUserRepository;

    UserController(AppUserService appUserService, AuthenticationManager authenticationManager,
                   JWTUtility jwtUtility, RestTemplate restTemplate, AppUserRepository appUserRepository){
        this.appUserService = appUserService;
        this.authenticationManager = authenticationManager;
        this.jwtUtility = jwtUtility;
        this.restTemplate = restTemplate;
        this.appUserRepository = appUserRepository;
    }

    @Value("${report.url}")
    private String reportUrl;

    /**
     * @param userSignUp - Type UserSignUp to take user signup form
     * @param role - user role value
     * @return ResponseEntity
     */
    @PostMapping("auth/signup/{role}")
    public ResponseEntity<?> clientSignUp(@RequestBody UserSignUp userSignUp, @PathVariable String role){

        return new ResponseEntity<>(this.appUserService.addClient(userSignUp, role), HttpStatus.CREATED);
    }

    /**
     * @param jwtRequest  - Type JwtRequest to take user signIn form
     * @return JwtResponse - A token for subsequent user requests
     * @throws Exception - Throws Exception if JwtRequest not verified
     */
    @PostMapping("auth/signin")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception{

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getEmail(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = appUserService.loadUserByUsername(jwtRequest.getEmail());

        final String token =
                jwtUtility.generateToken(userDetails);
        AppUser user = appUserRepository.getAppUserByEmail(userDetails.getUsername());
        JSONObject log = new JSONObject();
        log.put("userID", user.getId());
        log.put("authStatus", AuthStatus.LOGIN);
        log.put("role", user.getUserRole());
        log.put("localDateTime", LocalDateTime.now());

        HttpEntity<String> request = SendLoggerRequest.sendLoggerRequest(log);
        restTemplate.postForObject(reportUrl+"userAuthentication", request ,String.class);

        return  new JwtResponse(token);
    }


    /**
     * @param id - User id
     * @return ResponseEntity<?>
     */
    @GetMapping("getClientById/{id}")
    public ResponseEntity<?> getClient(@PathVariable Long id){
        Optional<AppUser> appUser = this.appUserService.getClient(id);
        if(appUser.isPresent()){
            return new ResponseEntity<>("client: " + appUser, HttpStatus.OK);
        }
        return new ResponseEntity<>("client with id "+ id + " does not exist", HttpStatus.NOT_FOUND);
    }


    /**
     * @param order - User order model type
     * @return String
     */
    @PostMapping("makeBuyOrder")
    public String makeBuyOrder(@RequestBody Order order){
        return this.appUserService.makeBuyOrder(order);
    }

    @PostMapping("createPortfolio")
    public String createPortfolio(@RequestBody CreatePortfolio createPortfolio){
        return appUserService.createPortfolio(createPortfolio);
    }

}
