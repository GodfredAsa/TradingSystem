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
        RestTemplate restTemplate = new RestTemplate();


        //Initial validation
//                Todo: Check if the product exist
        //        Todo: validate that the request body is not empty
        //        Todo: get the product name using the product id

        //Buy validation
//        Todo: Check the quantity by(X) the price should be less than the total account balance
//        Todo: Check the two exchanges for the best deal, buy all if quantity is >= then buy all else buy the rest from other exchange
//        Todo: Indication for partial purchase

        //Sell validation
//        Todo: Best deal (but orders more than the the specified price first)
//        Todo: Indication for partial purchase



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
