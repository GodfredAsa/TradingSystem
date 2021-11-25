package com.clientService.clientService.client.loggerPack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ClientLogger {
    public final Logger LOGGER = LoggerFactory.getLogger(ClientLogger.class);
}
