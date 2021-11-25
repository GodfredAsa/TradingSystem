package com.example.demo.marketdataservice;


import com.example.demo.logger.InfoLogger;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class MarketDataServiceController {
    private InfoLogger infoLogger;


    @GetMapping("/marketDataInfo/{message}")
    public void getMarketData(@PathVariable String message) {
            this.infoLogger.LOGGER.info("Current market data: " + message);
    }
}
