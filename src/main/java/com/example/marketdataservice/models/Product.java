package com.example.marketdataservice.models;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.core.serializer.Serializer;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @JsonProperty("LAST_TRADED_PRICE")
    private double lastTradedPrice;

    @JsonProperty("SELL_LIMIT")
    private int sellLimit;

    @JsonProperty("BID_PRICE")
    private double bidPrice;

    @JsonProperty("ASK_PRICE")
    private double askPrice;

    @JsonProperty("BUY_LIMIT")
    private int buyLimit;

    @JsonProperty("TICKER")
    private String ticker;

    @JsonProperty("MAX_PRICE_SHIFT")
    private double maxPriceShift;
}
