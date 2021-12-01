package com.example.marketdataservice.configs;

//import com.example.marketdataservice.controllers.Subscriber;
import com.example.marketdataservice.models.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.*;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RedisConfiguration {
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName("localhost");
        redisStandaloneConfiguration.setPort(9090);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("MarketDataChannel");
    }

    @Bean
    public RedisTemplate<String, Product> redisTemplate() {
        RedisTemplate<String, Product> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
//        template.setEnableDefaultSerializer(true);
        template.setDefaultSerializer(new Jackson2JsonRedisSerializer<Product>(Product.class));
        return template;
    }













































































































































































































































































//    @Bean
//    public MessageListenerAdapter messageListenerAdapter() {
//        return new MessageListenerAdapter(new Subscriber());
//    }

//    @Bean
//    public RedisMessageListenerContainer redisMessageListenerContainer() {
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        container.setConnectionFactory(jedisConnectionFactory());
//        container.addMessageListener(messageListenerAdapter(), channelTopic());
//        return container;
//    }
}
