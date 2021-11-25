package com.clientService.client.controller;

import com.clientService.client.service.ClientService;
import com.clientService.client.model.Client;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/client")
@AllArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/signup")
    public String clientSignUp(@RequestBody Client client){
        return this.clientService.addClient(client);
    }

    @GetMapping("/getClientById/{id}")
    public String getClient(@PathVariable Long id){
        Optional<Client> client = this.clientService.getClient(id);
        if(client.isPresent()){
            return "client: " + client;
        }
        return "client with id "+ id + " does not exist";
    }

//    @GetMapping
//    public Optional<Client> clientSignIn(@RequestBody )
}
