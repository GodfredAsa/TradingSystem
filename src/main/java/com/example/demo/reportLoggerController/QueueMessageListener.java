package com.example.demo.reportLoggerController;

import com.example.demo.entities.AuthenticationLog;
import com.example.demo.entities.QueueAuthenticationLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class QueueMessageListener {
    Logger logger = LoggerFactory.getLogger(QueueMessageListener.class);

    @JmsListener(destination = "authentication_log_queue")
            public void logUserAuthentication(QueueAuthenticationLog queueAuthenticationLog){
        logger.info("User Authentication Log: {}", queueAuthenticationLog);
    }
}
