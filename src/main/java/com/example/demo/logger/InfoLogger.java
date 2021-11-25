package com.example.demo.logger;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
    public class InfoLogger {
        public final Logger LOGGER = LoggerFactory.getLogger(InfoLogger.class);
    }

