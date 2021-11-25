package com.example.marketdataservice.controllers;

import com.example.marketdataservice.models.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@Api(value = "This is a collection of Market Data APIs", tags = "Market Data Resource")
public class MarketDataController {
    @Autowired
    private RestTemplate restTemplate;


    @ApiOperation("Get Market Data from Exchange 1")
    @GetMapping("/getMarketDataFromExchange1")
    public List<Product> getMarketDataFromExchange1(){
        String url = "https://exchange.matraining.com/md";
        List response = restTemplate.getForObject(url, List.class);

        return response;
    }

    @ApiOperation("Get Market Data from Exchange 2")
    @GetMapping("/getMarketDataFromExchange2")
    public List<Product> getMarketDataFromExchange2(){
        String url = "https://exchange2.matraining.com/md";
        List response = restTemplate.getForObject(url, List.class);

        return response;
    }
}
