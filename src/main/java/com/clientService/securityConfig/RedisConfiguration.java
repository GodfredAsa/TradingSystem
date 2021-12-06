package com.clientService.securityConfig;

import com.clientService.user.repository.MarketDataRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.Arrays;
import java.util.Map;


@NoArgsConstructor
@Configuration
public class RedisConfiguration {
    @Value("${spring.redis.hostname}")
    private String redisHostname;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.exchange1channel}")
    private String exchange1Channel;

    @Value("${spring.redis.exchange2channel}")
    private String exchange2Channel;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHostname);
        redisStandaloneConfiguration.setPort(redisPort);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic(exchange1Channel);
    }

    @Bean
    public ChannelTopic channelTopic2() {return new ChannelTopic(exchange2Channel);}

    @Bean
    public MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(new ExchangeSubscriber());
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());
        container.setMessageListeners(Map.of(messageListenerAdapter(), Arrays.asList(channelTopic(),channelTopic2())));
        return container;
    }
}
