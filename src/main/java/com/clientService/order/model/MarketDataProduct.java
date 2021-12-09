package com.clientService.order.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class MarketDataProduct {
    @JsonProperty("LAST_TRADED_PRICE")
    private double lastTradedPrice;

    @JsonProperty("SELL_LIMIT")
    private long sellLimit;

    @JsonProperty("BID_PRICE")
    private double bidPrice;

    @JsonProperty("ASK_PRICE")
    private double askPrice;

    @JsonProperty("BUY_LIMIT")
    private long buyLimit;

    @Id
    @JsonProperty("TICKER")
    private String ticker;

    @JsonProperty("MAX_PRICE_SHIFT")
    private double maxPriceShift;

}
