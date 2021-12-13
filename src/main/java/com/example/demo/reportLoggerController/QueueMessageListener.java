package com.example.demo.reportLoggerController;

import com.example.demo.entities.AuthenticationLog;
import com.example.demo.entities.OrderLog;
import com.example.demo.reportLoggerService.ReportLoggerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class QueueMessageListener {
    @Autowired
    private ReportLoggerService reportLoggerService;

    Logger logger = LoggerFactory.getLogger(QueueMessageListener.class);

    @JmsListener(destination = "authentication_log_queue")
    public void logUserAuthentication(String queueAuthenticationLog) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        AuthenticationLog authenticationLog = objectMapper.readValue(queueAuthenticationLog, AuthenticationLog.class);
        reportLoggerService.logUserAuthentication(authenticationLog);

        logger.info("User Authentication Log: {}", queueAuthenticationLog);
    }

    @JmsListener(destination = "order_log_queue")
    public void orders(String queueOrderLog) throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        OrderLog orderLog = objectMapper.readValue(queueOrderLog, OrderLog.class);
        reportLoggerService.orders(orderLog);

        logger.info("User Order Log: {}", queueOrderLog);

    }
}
