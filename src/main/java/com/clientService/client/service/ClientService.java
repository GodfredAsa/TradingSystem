package com.clientService.client.service;

import com.clientService.loggerPack.Logger;
import com.clientService.client.model.Client;
import com.clientService.client.repository.ClientRepository;
import com.clientService.order.model.OrderModel;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ClientService {
    private ClientRepository clientRepository;
    private RestTemplate restTemplate;

    public String addClient(Client client) {
        try {
                clientRepository.save(client);
                Logger.LOGGER.info("Client with id: " + client.getId() + " created successfully");
                restTemplate.getForObject("http://172.30.5.51:8080//clientSignInStatus/Success", String.class);
                return "Client added successfully";

        } catch (Exception e) {
            Logger.LOGGER.error(e.getMessage());
            restTemplate.getForObject("http://172.30.5.51:8080//clientSignInStatus/Failure", String.class);
            return "Client could not be added, try again";
        }
    }

    public Optional<Client> getClient(Long id){
        Logger.LOGGER.info("Client with id:" +id + " accessed from the database");
        return clientRepository.findById(id);
    }

    public String makeBuyOrder(OrderModel order){

            String response = restTemplate.postForObject("http://172.19.128.1:8080/api/order/makeBuyOrder", order, String.class);
            if (response != null){
                Logger.LOGGER.info("order successful");
                return response;
            }else{
                Logger.LOGGER.error("There was an issue placing your order");
                return "There was an issue placing your order, please try again later";
            }
    }
}
