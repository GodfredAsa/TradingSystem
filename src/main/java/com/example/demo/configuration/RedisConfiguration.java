package com.example.demo.configuration;

import com.example.demo.entities.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisConfiguration {
    @Value("${redis.hostname}")
    private String redisHostname;

    @Value("${redis.port}")
    private int redisPort;

    @Value("${redis.marketdatachannel}")
    private String redisMarketDataChannel;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHostname);
        redisStandaloneConfiguration.setPort(redisPort);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic(redisMarketDataChannel);
    }

    @Bean
    public RedisTemplate<String, Product> redisTemplate() {
        RedisTemplate<String, Product> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setDefaultSerializer(new Jackson2JsonRedisSerializer<Product>(Product.class));
        return template;
    }
}
