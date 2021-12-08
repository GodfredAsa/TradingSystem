package com.example.demo.reportLoggerController;

import com.example.demo.entities.AuthenticationLog;
import com.github.sonus21.rqueue.annotation.RqueueListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueueMessageListener {
    Logger logger = LoggerFactory.getLogger(QueueMessageListener.class);

    @RqueueListener(value = "authentication-log-queue")
    public void logUserAuthentication(AuthenticationLog authenticationLog){
        logger.info("User Authentication Log: {}", authenticationLog);
    }
}
