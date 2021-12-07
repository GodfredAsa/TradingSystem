package com.clientService.user.service;

import com.clientService.enums.PortfolioStatus;
import com.clientService.loggerPack.LoggerConfig;
import com.clientService.user.model.AppUser;
import com.clientService.user.model.CreatePortfolio;
import com.clientService.user.model.HttpResponseBody;
import com.clientService.user.model.Portfolio;
import com.clientService.user.repository.AppUserRepository;
import com.clientService.user.repository.PortfolioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final HttpResponseBody response;
    private final PortfolioRepository portfolioRepository;

    public AppUserService(AppUserRepository appUserRepository, HttpResponseBody response,
                          PortfolioRepository portfolioRepository) {
        this.appUserRepository = appUserRepository;
        this.response = response;
        this.portfolioRepository = portfolioRepository;
    }

    /**
     * @param id -User id
     * @return Optional</ User>
     */
    public ResponseEntity<?> getClient(Long id) {
        Optional<AppUser> user = appUserRepository.findById(id);

        if (user.isPresent()) {
            LoggerConfig.LOGGER.info("Client with id: " + id + " accessed from the database");
            response.setMessage(user.get());
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        }

        LoggerConfig.LOGGER.info("Client with id:" + id + " does not exist");
        response.setMessage("client with id "+ id + " does not exist");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    public AppUser getAppUserByEmail(String email) {
        return appUserRepository.getAppUserByEmail(email);
    }

    public String createPortfolio(CreatePortfolio createPortfolio) {
        Optional<AppUser> appUser = appUserRepository.findById(createPortfolio.getId());

        if(appUser.isPresent()){
            portfolioRepository.save(new Portfolio(
                    appUser.get(), PortfolioStatus.OPENED
            ));
            return "portfolio created";
        }

        return "Unsuccessful portfolio creation, user does not exist";

    }
}
