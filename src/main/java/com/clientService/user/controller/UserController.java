package com.clientService.user.controller;

import com.clientService.user.model.*;
import com.clientService.user.service.AppUserService;
import com.clientService.order.model.OrderModel;

import com.clientService.securityConfig.JWTUtility;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/client/")
public class UserController {

    private final AppUserService clientService;
    private JWTUtility jwtUtility;
    private AuthenticationManager authenticationManager;

    /**
     * @param userSignUp - Type UserSignUp to take user signup form
     * @param role - user role value
     * @return ResponseEntity
     */
    @PostMapping("/auth/signup/{role}")
    public ResponseEntity<?> clientSignUp(@RequestBody UserSignUp userSignUp, @PathVariable String role){

        return new ResponseEntity<>(this.clientService.addClient(userSignUp, role), HttpStatus.CREATED);
    }

    /**
     * @param jwtRequest  - Type JwtRequest to take user signIn form
     * @return JwtResponse - A token for subsequent user requests
     * @throws Exception - Throws Exception if JwtRequest not verified
     */
    @PostMapping("/auth/signin")
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

        final UserDetails userDetails
                = clientService.loadUserByUsername(jwtRequest.getEmail());

        final String token =
                jwtUtility.generateToken(userDetails);

        return  new JwtResponse(token);
    }


    /**
     * @param id - User id
     * @return ResponseEntity<?>
     */
    @GetMapping("getClientById/{id}")
    public ResponseEntity<?> getClient(@PathVariable Long id){
        Optional<AppUser> client = this.clientService.getClient(id);
        if(client.isPresent()){
            return new ResponseEntity<>("client: " + client, HttpStatus.OK);
        }
        return new ResponseEntity<>("client with id "+ id + " does not exist", HttpStatus.NOT_FOUND);
    }


    /**
     * @param order - User order model type
     * @return String
     */
    @PostMapping("makeBuyOrder")
    public String makeBuyOrder(@RequestBody OrderModel order){
        return this.clientService.makeBuyOrder(order);
    }

    @PostMapping("createPortfolio")
    public String createPortfolio(@RequestBody CreatePortfolio createPortfolio){
        return clientService.createPortfolio(createPortfolio);
    }

}
