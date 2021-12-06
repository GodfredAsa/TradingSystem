package com.clientService.securityConfig;


import com.clientService.order.model.MarketDataProduct;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class ExchangeSubscriber implements MessageListener {
    Logger logger =  LoggerFactory.getLogger(ExchangeSubscriber.class);

    @SneakyThrows
    @Override
    public void onMessage(Message message, byte[] pattern) {
        if (new String(message.getChannel(), StandardCharsets.UTF_8).equals("exchange1")){
            ObjectMapper objectMapper = new ObjectMapper();
            String body = new String(message.getBody());

            MarketDataProduct[] msg = objectMapper.readValue(body, MarketDataProduct[].class);
            logger.info("Consumed Message1 {}", Arrays.asList(msg));

        } else if(new String(message.getChannel(), StandardCharsets.UTF_8).equals("exchange2")) {
            ObjectMapper objectMapper = new ObjectMapper();
            String body = new String(message.getBody());

            MarketDataProduct[] msg = objectMapper.readValue(body, MarketDataProduct[].class);
            logger.info("Consumed Message2 {}", Arrays.asList(msg));
        }
    }
}