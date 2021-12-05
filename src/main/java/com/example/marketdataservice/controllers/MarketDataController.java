package com.example.marketdataservice.controllers;

import com.example.marketdataservice.models.Product;
import com.example.marketdataservice.services.MarketDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

@RestController
@Api(value = "This is a collection of Market Data APIs", tags = "Market Data Resource")
public class MarketDataController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MarketDataService marketDataService;

    @ApiOperation("Get Market Data from Exchange 1")
    @GetMapping("/getMarketDataFromExchange1")
    public ResponseEntity<List<Product>> getMarketDataFromExchange1() throws UnknownHostException {
        return new ResponseEntity<>(marketDataService.getMarketDataFromExchange1(), HttpStatus.OK);
    }

    @ApiOperation("Get Market Data from Exchange 2")
    @GetMapping("/getMarketDataFromExchange2")
    public ResponseEntity<List<Product>> getMarketDataFromExchange2(){
        return new ResponseEntity<>(marketDataService.getMarketDataFromExchange2(), HttpStatus.OK);
    }
}
