package com.example.demo.reportLoggerController;

import com.example.demo.entities.AuthenticationLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;

public class QueueMessageListener {
    Logger logger = LoggerFactory.getLogger(QueueMessageListener.class);

    @JmsListener(destination = "authentication_log_queue")
    public void logUserAuthentication(AuthenticationLog authenticationLog){
        logger.info("User Authentication Log: {}", authenticationLog);
    }
}
