package com.clientService.user.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueuePublisher {

    @Autowired
    private  JmsTemplate jmsTemplate;

    public  String publishToQueue(String queue, Object message){
        jmsTemplate.convertAndSend(queue, message);
        return "Message successfully sent to " + queue;
    }
}
