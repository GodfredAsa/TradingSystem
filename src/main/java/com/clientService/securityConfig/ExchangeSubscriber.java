package com.clientService.securityConfig;


import com.clientService.order.model.MarketDataProduct;
import com.clientService.order.service.CachedMarketDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class ExchangeSubscriber implements MessageListener {
    Logger logger = LoggerFactory.getLogger(ExchangeSubscriber.class);

    CachedMarketDataService cachedMarketDataService = new CachedMarketDataService(new RestTemplateBuilder());

    @SneakyThrows
    @Override
    public void onMessage(Message message, byte[] pattern) {
        if (new String(message.getChannel(), StandardCharsets.UTF_8).equals("exchange1")) {
            ObjectMapper objectMapper = new ObjectMapper();
            String body = new String(message.getBody());

            MarketDataProduct[] msg = objectMapper.readValue(body, MarketDataProduct[].class);
            List<MarketDataProduct> msgList = Arrays.asList(msg);
            cachedMarketDataService.clearMarketDataE1();
            cachedMarketDataService.setMarketDataE1(msgList);
            logger.info("Consumed Message1 {}", msgList);

        } else if (new String(message.getChannel(), StandardCharsets.UTF_8).equals("exchange2")) {
            ObjectMapper objectMapper = new ObjectMapper();
            String body = new String(message.getBody());

            MarketDataProduct[] msg = objectMapper.readValue(body, MarketDataProduct[].class);
            List<MarketDataProduct> msgList = Arrays.asList(msg);
            cachedMarketDataService.clearMarketDataE2();
            cachedMarketDataService.setMarketDataE2(msgList);
            logger.info("Consumed Message2 {}", msgList);
        }
    }
}