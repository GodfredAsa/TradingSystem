package com.clientService.order.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketDataProduct {
    @JsonProperty("LAST_TRADED_PRICE")
    private String lastTradedPrice;

    @JsonProperty("SELL_LIMIT")
    private double sellLimit;

    @JsonProperty("BID_PRICE")
    private double bidPrice;

    @JsonProperty("ASK_PRICE")
    private int askPrice;

    @JsonProperty("BUY_LIMIT")
    private String buyLimit;

    @JsonProperty("TICKER")
    private String ticker;

    @JsonProperty("MAX_PRICE_SHIFT")
    private int maxPriceShift;
}
