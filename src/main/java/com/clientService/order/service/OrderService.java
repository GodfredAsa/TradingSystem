package com.clientService.order.service;

import com.clientService.loggerPack.LoggerConfig;
import com.clientService.order.model.OrderModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderService {

    @Value("${api.key}")
    private String apiKey;

    @Value("${exchange.url1}")
    private String exchangeUrl1;


    public String makeOrder(OrderModel order) {

//        Todo: validate if user has enough fund to make the purchase or owns the stocks they intend to sell
        RestTemplate restTemplate = new RestTemplate();

//        Todo: validate that the request body is not empty
//        Todo: get the product name using the product id

        Map<String, Object> orderRequestBody = new HashMap<>() {{
            put("product", order.getProduct());
            put("quantity", order.getOrd_quantity());
            put("price", order.getOrd_price());
        }};

        if (order.getOrd_side().equals("BUY")) {
            orderRequestBody.put("side", "BUY");
        } else {
            orderRequestBody.put("side", "SELL");
        }

        String response = restTemplate.postForObject(exchangeUrl1 + apiKey + "/order", orderRequestBody, String.class);
        if (response != null){
            LoggerConfig.LOGGER.info("order placed successfully");
            return response;
        }else{
            LoggerConfig.LOGGER.error("There was an issue placing your order");
            return "There was an issue placing your order, please try again later";
        }
    }

}
