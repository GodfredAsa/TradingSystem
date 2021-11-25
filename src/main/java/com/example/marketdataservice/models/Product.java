package com.example.marketdataservice.models;

import lombok.Data;

@Data
public class Product {
    private String lastTradedPrice;
    private double sellLimit;
    private double bidPrice;
    private int askPrice;
    private String buyLimit;
    private String ticker;
    private int maxPriceShift;
}
