package com.example.demo.reportLoggerController;

import com.example.demo.entities.AuthenticationLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueueMessageListener {
    Logger logger = LoggerFactory.getLogger(QueueMessageListener.class);

    public void logUserAuthentication(AuthenticationLog authenticationLog){
        logger.info("User Authentication Log: {}", authenticationLog);
    }
}
