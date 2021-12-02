package com.example.demo.reportLoggerService;


import com.example.demo.entities.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Subscriber implements MessageListener {

    Logger logger =  LoggerFactory.getLogger(Subscriber.class);

    @SneakyThrows
    @Override
    public void onMessage(Message message, byte[] pattern) {
        ObjectMapper objectMapper = new ObjectMapper();
        String body = new String(message.getBody());

        Product[] msg = objectMapper.readValue(body, Product[].class);
        logger.info("Consumed Message {}", Arrays.asList(msg));
    }
}
