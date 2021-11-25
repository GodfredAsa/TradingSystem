package com.clientService.order.service;

import com.clientService.order.model.OrderModel;
import com.clientService.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Value("${api.key}")
    private String exchangeAPIOrderUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public String makeBuyOrder(OrderModel order) {

//        Todo: validate if user has enough fund to make the purchase
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");

//        Todo: validate that the request body is not empty
//        Todo: get the product name using the product id

        Map<String, Object> orderRequestBody = new HashMap<>() {{
            put("product", order.getProduct());
            put("quantity", order.getOrd_quantity());
            put("price", order.getOrd_price());
            put("side", "BUY");
        }};
        String response = restTemplate.postForObject(this.exchangeAPIOrderUrl, orderRequestBody, String.class);
        if (response != null)
            return response;

        throw new IllegalStateException("There was an issue placing your order, please try again later");
    }


    public String makeSellOrder(OrderModel order) {
//        Todo: validate if user owns the stocks they intend to sell

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", "application/json");

//        Todo: validate that the request body is not empty
//        Todo: get the product name using the product id

        Map<String, Object> orderRequestBody = new HashMap<>() {{
            put("product", order.getProduct());
            put("quantity", order.getOrd_quantity());
            put("price", order.getOrd_price());
            put("side", "SELL");
        }};

        String response = restTemplate.postForObject(this.exchangeAPIOrderUrl, orderRequestBody, String.class);
        if (response != null)
            return response;

        throw new IllegalStateException("There was an issue placing your order, please try again later");
    }
}
