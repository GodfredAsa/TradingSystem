package com.example.marketdataservice.controllers;

import com.example.marketdataservice.models.Product;
import com.example.marketdataservice.services.MarketDataService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RedisMessagePublisher {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ChannelTopic channelTopic;

    @Autowired
    private ChannelTopic channelTopic2;

    @ApiOperation("Subscription to Exchange1 Data")
    @PostMapping("/publish") //TODO: Subscribe to Exchange1 with URL
    public String publish(@RequestBody List<Product> message) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
        return "Exchange1 Data Published";
    }


    @ApiOperation("Subscription to Exchange2 Data")
    @PostMapping("/publish2") //TODO: Subscribe to Exchange2 with URL
    public String publish2(@RequestBody List<Product> message) {
        redisTemplate.convertAndSend(channelTopic2.getTopic(), message);
        return "Exchange2 Data Published";
    }
}


