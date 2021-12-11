package com.clientService.securityConfig;

import antlr.PreservingFileWriter;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableJms
public class ActiveMQConfiguration {
    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL("tcp://167.99.202.174:61616");
        return activeMQConnectionFactory;
    }

    @Bean
    public JmsTemplate template() {
        return new JmsTemplate(activeMQConnectionFactory());
    }
}
