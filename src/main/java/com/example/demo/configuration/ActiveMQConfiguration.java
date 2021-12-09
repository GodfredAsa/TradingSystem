package com.example.demo.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableJms
public class ActiveMQConfiguration {
    private String DEFAULT_BROKER_URL = "tcp://localhost:61616";

    @Bean
    public ConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(DEFAULT_BROKER_URL);
        activeMQConnectionFactory.setTrustedPackages(List.of("com.example"));
        activeMQConnectionFactory.setUserName("admin");
        activeMQConnectionFactory.setPassword("admin");
        return activeMQConnectionFactory;
    }


}
