package com.clientService.client.controller;

import com.clientService.client.service.ClientService;
import com.clientService.client.model.Client;
import com.clientService.order.model.OrderModel;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/client/")
@AllArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping("signup")
    public ResponseEntity<?> clientSignUp(@RequestBody Client client){

        return new ResponseEntity<>(this.clientService.addClient(client), HttpStatus.CREATED);
    }

    @GetMapping("getClientById/{id}")
    public ResponseEntity<?> getClient(@PathVariable Long id){
        Optional<Client> client = this.clientService.getClient(id);
        if(client.isPresent()){
            return new ResponseEntity<>("client: " + client, HttpStatus.OK);
        }
        return new ResponseEntity<>("client with id "+ id + " does not exist", HttpStatus.NOT_FOUND);
    }

    @PostMapping("makeBuyOrder")
    public String makeBuyOrder(@RequestBody OrderModel order){
        return this.clientService.makeBuyOrder(order);
    }

}
