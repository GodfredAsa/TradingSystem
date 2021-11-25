package com.example.demo.clientservice;

import com.example.demo.logger.InfoLogger;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class ClientServiceController {
    private InfoLogger infoLogger;

    @GetMapping("/clientSignInStatus/{message}")
    public void clientSignIn(@PathVariable String message) {
        if (message.equals("Success")) {
            this.infoLogger.LOGGER.info("Client signed in successfully");
        } else {
            this.infoLogger.LOGGER.warn("Client login failed!");
        }

    }
}