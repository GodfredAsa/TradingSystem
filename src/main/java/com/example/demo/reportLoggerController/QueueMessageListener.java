package com.example.demo.reportLoggerController;

import com.example.demo.entities.AuthenticationLog;
import com.example.demo.entities.Product;
import com.example.demo.entities.QueueAuthenticationLog;
import com.example.demo.entities.QueueOrderLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class QueueMessageListener {
    ObjectMapper objectMapper = new ObjectMapper();

    Logger logger = LoggerFactory.getLogger(QueueMessageListener.class);

    @JmsListener(destination = "authentication_log_queue")
    public void logUserAuthentication(String queueAuthenticationLog) throws JsonProcessingException {
        QueueAuthenticationLog authenticationLog = objectMapper.readValue(queueAuthenticationLog, QueueAuthenticationLog.class);
        System.out.println(authenticationLog);
        logger.info("User Authentication Log: {}", queueAuthenticationLog);
    }

    @JmsListener(destination = "order_log_queue")
    public void orders(String queueOrderLog) {
        logger.info(" Order Log: {}", queueOrderLog);
    }

}
