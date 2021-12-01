package com.clientService.user.service;

import com.clientService.enums.PortfolioStatus;
import com.clientService.loggerPack.LoggerConfig;
import com.clientService.enums.UserRole;
import com.clientService.user.model.AppUser;
import com.clientService.user.model.CreatePortfolio;
import com.clientService.user.model.Portfolio;
import com.clientService.user.model.UserSignUp;
import com.clientService.user.repository.PortfolioRepository;
import com.clientService.user.repository.UserRepository;
import com.clientService.order.model.OrderModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppUserService implements UserDetailsService {
    private final UserRepository clientRepository;
    private final RestTemplate restTemplate;
    private final PortfolioRepository portfolioRepository;

    @Value("${report.url}")
    private String reportUrl;

    @Value("${order.url}")
    private String orderUrl;


    /**
     * @param clientRepository ;
     * @param restTemplate;
     */
    @Autowired
    AppUserService(UserRepository clientRepository, RestTemplate restTemplate, PortfolioRepository portfolioRepository){
        this.clientRepository = clientRepository;
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
        AppUser client = clientRepository.getUserByEmail(email);
        if(client == null){
            LoggerConfig.LOGGER.error("Client does not exist");
            throw new UsernameNotFoundException("Client does not exist");
        }else{
            LoggerConfig.LOGGER.info("Client found");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(client.getUserRole().name()));
        return new org.springframework.security.core.userdetails.User(client.getEmail(), client.getPassword(), authorities);
    }


    /**
     * @param userSignUp - UserSignUp Model
     * @param role - UserRole
     * @return String
     */
    public String addClient(UserSignUp userSignUp, String role) {
        try {

            UserRole appUserRole = null;
            if(role.equals("client")){
                appUserRole = UserRole.CLIENT;
            }else if(role.equals("admin")){
                appUserRole = UserRole.ADMIN;
            }else{
                appUserRole =UserRole.REGULATOR;
            }

                AppUser user = clientRepository.save(
                        new AppUser(
                                userSignUp.getFirstName(), userSignUp.getLastName(),
                                userSignUp.getDateOfBirth(), userSignUp.getEmail(),
                                userSignUp.getPassword(), userSignUp.getContact(), appUserRole
                        )
                );
                LoggerConfig.LOGGER.info("Client with id: " + user.getId() + " created successfully");
                restTemplate.getForObject(reportUrl+"/Success", String.class);
                return "Client added successfully";

        } catch (Exception e) {
            LoggerConfig.LOGGER.error(e.getMessage());
            restTemplate.getForObject(reportUrl+"/Failure", String.class);
            return "Client could not be added, try again";
        }
    }


    /**
     * @param id -User id
     * @return Optional</User>
     */
    public Optional<AppUser> getClient(Long id){
        Optional<AppUser> user = clientRepository.findById(id);
        if(user.isPresent()){
            LoggerConfig.LOGGER.info("Client with id:" +id + " accessed from the database");
        }else{
            LoggerConfig.LOGGER.info("Client with id:" +id + " does not exist");
        }
        return  user;
    }

    /**
     * @param order - OrderModel Type
     * @return String
     */
    public String makeBuyOrder(OrderModel order){

            String response = restTemplate.postForObject(orderUrl+"/makeBuyOrder", order, String.class);
            if (response != null){
                LoggerConfig.LOGGER.info("order successful");
                return response;
            }else{
                LoggerConfig.LOGGER.error("There was an issue placing your order");
                return "There was an issue placing your order, please try again later";
            }
    }

    public String createPortfolio(CreatePortfolio createPortfolio) {
        AppUser appUser = clientRepository.findById(createPortfolio.getId()).get();
        portfolioRepository.save(new Portfolio(
              appUser, PortfolioStatus.OPENED
        ));
        return "porfolio created";
    }
}
