package com.example.demo.reportLoggerService;


import com.example.demo.entities.Product;
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

            Product[] msg = objectMapper.readValue(body, Product[].class);
            logger.info("Consumed Message1 {}", Arrays.asList(msg));

        } else if(new String(message.getChannel(), StandardCharsets.UTF_8).equals("exchange2")) {
            ObjectMapper objectMapper = new ObjectMapper();
            String body = new String(message.getBody());

            Product[] msg = objectMapper.readValue(body, Product[].class);
            logger.info("Consumed Message2 {}", Arrays.asList(msg));

        }
    }
}