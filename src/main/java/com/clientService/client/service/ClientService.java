package com.clientService.client.service;

import com.clientService.client.loggerPack.ClientLogger;
import com.clientService.client.model.Client;
import com.clientService.client.repository.ClientRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ClientService {
    private ClientRepository clientRepository;
    private ClientLogger clientLogger;
//    private SecurityConfiguration securityConfig;

    public String addClient(Client client) {
        try {
//            String encodedPassword = securityConfig.passwordEncoder().encode(client.getPassword());
//            client.setPassword(encodedPassword);
                clientRepository.save(client);
                clientLogger.LOGGER.info("Client with id: " + client.getId() + " created successfully");
                return "Client added successfully";

        } catch (Exception e) {
            clientLogger.LOGGER.error(e.getMessage());
            return "Client could not be added, try again";
        }

    }

    public Optional<Client> getClient(Long id){
        clientLogger.LOGGER.info("Client with id:" +id + " accessed from the database");
        return clientRepository.findById(id);
    }
}
