package com.clientService.user.controller;

import com.clientService.user.model.*;
import com.clientService.user.service.AppUserAuthService;

import com.clientService.user.service.AppUserService;
import com.clientService.user.service.AppUserSignInService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/client/")
public class UserController {
    private final AppUserAuthService appUserAuthService;
    private final AppUserService appUserService;
    private final AppUserSignInService appUserSignInService;


    UserController(AppUserAuthService appUserAuthService, AppUserService appUserService,
                   AppUserSignInService appUserSignInService){
        this.appUserAuthService = appUserAuthService;
        this.appUserService = appUserService;
        this.appUserSignInService = appUserSignInService;
    }

    /**
     * @param userSignUp - Type UserSignUp to take user signup form
     * @param role - user role value
     * @return ResponseEntity
     */
    @PostMapping("auth/signup/{role}")
    public ResponseEntity<?> userSignUp(@RequestBody UserSignUp userSignUp, @PathVariable String role){
        return this.appUserAuthService.registerUser(userSignUp, role);
    }

    /**
     * @param jwtRequest  - Type JwtRequest to take user signIn form
     * @return ResponseEntity - A token for subsequent user requests
     *
     */

    @PostMapping("auth/signin")
    public ResponseEntity<?> authenticate(@RequestBody JwtRequest jwtRequest){
        return this.appUserSignInService.authenticateUser(jwtRequest);
    }


    /**
     * @param id - User id
     * @return ResponseEntity<?>
     */
    @GetMapping("getClientById/{id}")
    public ResponseEntity<?> getClient(@PathVariable Long id){
        return  this.appUserService.getClient(id);
    }


    @GetMapping("logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetails appPrincipal){
        // TODO implement the logout logic
        return ResponseEntity.ok("Logout, " + appPrincipal.getUsername());
    }


//    /**
//     * @param order - User order model type
//     * @return String
//     */
//    @PostMapping("makeBuyOrder")
//    public String makeBuyOrder(@RequestBody OrderModel order){
//        return this.appUserService.makeBuyOrder(order);
//    }
//

//    @PostMapping("createPortfolio")
//    public String createPortfolio(@RequestBody CreatePortfolio createPortfolio){
//        return appUserService.createPortfolio(createPortfolio);
//    }


    //    @GetMapping(value = "/username")
//    public String currentUserNameSimple(HttpServletRequest request) {
//        Principal principal = request.getUserPrincipal();
//        System.out.println(principal.getName());
//        return principal.getName();
//    }
}
