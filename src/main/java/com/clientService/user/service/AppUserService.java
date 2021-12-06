package com.clientService.user.service;

import com.clientService.enums.AuthStatus;
import com.clientService.enums.PortfolioStatus;
import com.clientService.loggerPack.LoggerConfig;
import com.clientService.enums.UserRole;
import com.clientService.securityConfig.SendLoggerRequest;
import com.clientService.user.model.*;
import com.clientService.user.repository.AccountRepository;
import com.clientService.user.repository.PortfolioRepository;
import com.clientService.user.repository.AppUserRepository;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final AccountRepository accountRepository;
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
     * @param portfolioRepository - portfolio repository
     */
    AppUserService(AppUserRepository appUserRepository, AccountRepository accountRepository,
                   RestTemplate restTemplate, PortfolioRepository portfolioRepository) {
        this.appUserRepository = appUserRepository;
        this.accountRepository = accountRepository;
        this.restTemplate = restTemplate;
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

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(appUser.getUserRole().name()));
        return new org.springframework.security.core.userdetails.User(appUser.getEmail(), appUser.getPassword(), authorities);
    }


    /**
     * @param userSignUp - UserSignUp Model
     * @param role       - UserRole
     * @return String
     */
    public String addClient(UserSignUp userSignUp, String role) {
        try {

            UserRole appUserRole = null;
            if (role.equals("client")) {
                appUserRole = UserRole.CLIENT;
            } else if (role.equals("admin")) {
                appUserRole = UserRole.ADMIN;
            } else {
                appUserRole = UserRole.REGULATOR;
            }


            AppUser user = appUserRepository.save(
                    new AppUser(
                            userSignUp.getFirstName(), userSignUp.getLastName(),
                            userSignUp.getEmail(), userSignUp.getPassword(),
                            appUserRole
                    )
            );

            accountRepository.save(new Account(100000.0, user));

            LoggerConfig.LOGGER.info("Client with id: " + user.getId() + " created successfully");

            JSONObject log = new JSONObject();
            log.put("userID", user.getId());
            log.put("authStatus", AuthStatus.REGISTER);
            log.put("role", user.getUserRole());
            log.put("localDateTime", LocalDateTime.now());

            HttpEntity<String> request = SendLoggerRequest.sendLoggerRequest(log);
//            restTemplate.postForObject(reportUrl + "userAuthentication", request, String.class);
            return "Client added successfully";

        } catch (Exception e) {
            LoggerConfig.LOGGER.error(e.getMessage());
            return "Client could not be added, try again";
        }
    }


    /**
     * @param id -User id
     * @return Optional</ User>
     */
    public Optional<AppUser> getClient(Long id) {
        Optional<AppUser> user = appUserRepository.findById(id);
        if (user.isPresent()) {
            LoggerConfig.LOGGER.info("Client with id:" + id + " accessed from the database");
        } else {
            LoggerConfig.LOGGER.info("Client with id:" + id + " does not exist");
        }
        return user;
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
    public String createPortfolio(CreatePortfolio createPortfolio) {
        AppUser appUser = appUserRepository.findById(createPortfolio.getId()).get();
        portfolioRepository.save(new Portfolio(
                appUser, PortfolioStatus.OPENED
        ));
        return "portfolio created";
    }

    public AppUser getAppUserByEmail(String email) {
        AppUser appUser = appUserRepository.getAppUserByEmail(email);
        return appUser;
    }

}
