package com.example.demo.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;
import java.util.Arrays;
import java.util.List;

@Configuration
public class ActiveMQConfiguration {
    private String DEFAULT_BROKER_URL = "tcp://localhost:61616";
    private String MESSAGE_QUEUE = "authentication-log-queue";

    public ConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(DEFAULT_BROKER_URL);
        activeMQConnectionFactory.setTrustedPackages(List.of("com.example"));
        return activeMQConnectionFactory;
    }

}
