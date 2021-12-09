package com.clientService.user.service;

import com.clientService.enums.AuthStatus;
import com.clientService.enums.PortfolioStatus;
import com.clientService.loggerPack.LoggerConfig;
import com.clientService.enums.UserRole;
import com.clientService.securityConfig.EmailValidation;
import com.clientService.securityConfig.SendLoggerRequest;
import com.clientService.user.model.*;
import com.clientService.user.repository.AccountRepository;
import com.clientService.user.repository.AppUserRepository;

import com.clientService.user.repository.PortfolioRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class AppUserAuthService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final AccountRepository accountRepository;
    private final HttpResponseBody response;
    private final RestTemplate restTemplate;
    private final PortfolioRepository portfolioRepository;


    @Value("${report.url}")
    private String reportUrl;

    @Value("${order.url}")
    private String orderUrl;


    /**
     * @param appUserRepository   - application user repository
     * @param accountRepository   - account repository
     * @param restTemplate        - restTemplate
     * @param response - response body type
     * @param portfolioRepository - portfolio repository
     */
    AppUserAuthService(AppUserRepository appUserRepository, AccountRepository accountRepository,
                       RestTemplate restTemplate, HttpResponseBody response, PortfolioRepository portfolioRepository) {
        this.appUserRepository = appUserRepository;
        this.accountRepository = accountRepository;
        this.restTemplate = restTemplate;
        this.response = response;
        this.portfolioRepository = portfolioRepository;
    }


    /**
     * @param email - takes user email to validate user
     * @return UserDetails
     * @throws UsernameNotFoundException - Throws when use does not exist
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.getAppUserByEmail(email);
        if (appUser == null) {
            LoggerConfig.LOGGER.error("Client does not exist");
            throw new UsernameNotFoundException("Client does not exist");
        } else {
            LoggerConfig.LOGGER.info("Client found");
        }

        return new AppUserDetails(appUser);
    }


    /**
     * @param userSignUp - UserSignUp Model
     * @param role       - UserRole
     * @return String
     */
    public ResponseEntity<?> registerUser(UserSignUp userSignUp, String role) {

        try {
            UserRole appUserRole = role.equals("client")? UserRole.CLIENT :
                    role.equals("admin")? UserRole.ADMIN : UserRole.REGULATOR;

            if(userSignUp.getEmail() == null || userSignUp.getLastName() == null ||
               userSignUp.getFirstName() == null || userSignUp.getPassword() == null){
                response.setMessage("Check request body fields, some are empty");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            if(!EmailValidation.patternMatches(userSignUp.getEmail())){
                response.setMessage("Incorrect email format");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            if(appUserRepository.getCountOfEmail(userSignUp.getEmail()) > 0){
                response.setMessage("Email already exist");
                return new ResponseEntity<>(response, HttpStatus.valueOf(409));
            }

            AppUser user = appUserRepository.save(
                    new AppUser(
                            userSignUp.getFirstName(), userSignUp.getLastName(),
                            userSignUp.getEmail(), userSignUp.getPassword(),
                            appUserRole
                    )
            );

            accountRepository.save(new Account(100000.0, user));
            portfolioRepository.save(new Portfolio(user, PortfolioStatus.OPENED));


            LoggerConfig.LOGGER.info("Client with id: " + user.getId() + " created successfully");

            JSONObject log = new JSONObject();
            log.put("userID", user.getId());
            log.put("authStatus", AuthStatus.REGISTER);
            log.put("role", user.getUserRole());
            log.put("localDateTime", LocalDateTime.now());

            HttpEntity<String> request = SendLoggerRequest.sendLoggerRequest(log);
//            restTemplate.postForObject(reportUrl + "userAuthentication", request, String.class);

            response.setMessage("User added successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            LoggerConfig.LOGGER.error(e.getMessage());
            response.setMessage("Client could not be added, try again");
            return new ResponseEntity<>( response, HttpStatus.valueOf(500));
        }
    }



    /**
     * @param - OrderModel Type
     * @return String
     */
//    public String makeBuyOrder(Order order){
//
//            String response = restTemplate.postForObject(orderUrl+"/makeBuyOrder", order, String.class);
//            if (response != null){
//                LoggerConfig.LOGGER.info("order successful");
//                return response;
//            }else{
//                LoggerConfig.LOGGER.error("There was an issue placing your order");
//                return "There was an issue placing your order, please try again later";
//            }
//    }

}
