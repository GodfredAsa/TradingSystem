package com.example.marketdataservice.services;

import com.example.marketdataservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MarketDataService {
    @Autowired
    private RestTemplate restTemplate;

    public List<Product> getMarketDataFromExchange1(){
        String url = "https://exchange.matraining.com/md";
        List response = restTemplate.getForObject(url, List.class);

        return response;
    }

    public List<Product> getMarketDataFromExchange2(){
        String url = "https://exchange2.matraining.com/md";
        List response = restTemplate.getForObject(url, List.class);

        return response;
    }
}
