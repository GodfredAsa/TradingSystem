package com.example.marketdataservice.services;

import com.example.marketdataservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

@Service
public class MarketDataService {
    @Autowired
    private RestTemplate restTemplate;

    public List<Product> getMarketDataFromExchange1() throws UnknownHostException {
        String url = "https://exchange.matraining.com/md";
        List<Product> response = restTemplate.getForObject(url, List.class);
        System.out.println(InetAddress.getLocalHost().getHostAddress());

        return response;
    }

    public List<Product> getMarketDataFromExchange2(){
        String url = "https://exchange2.matraining.com/md";
        List<Product> response = restTemplate.getForObject(url, List.class);
        System.out.println(response);

        return response;
    }
}
