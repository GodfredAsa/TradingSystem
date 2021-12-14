package com.example.demo.configuration;

import com.example.demo.reportLoggerService.ExchangeSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.Arrays;
import java.util.Map;

@Configuration
public class RedisConfiguration {
    @Value("${redis.exchange1channel}")
    private String exchange1Channel;

    @Value("${redis.exchange2channel}")
    private String exchange2Channel;

    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic(exchange1Channel);
    }

    @Bean
    public ChannelTopic channelTopic2() {
        return new ChannelTopic(exchange2Channel);
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(new ExchangeSubscriber());
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.setMessageListeners(Map.of(messageListenerAdapter(), Arrays.asList(channelTopic(), channelTopic2())));
        return container;
    }
}
