package com.example.demo.entities;

import com.example.demo.enums.TradeSide;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
public class QueueOrderLog implements Serializable {
    private Long userID;
    private String exchangeOrderID;
    private int productID;
    private int quantity;
    private Double price;
    private TradeSide side;
    private LocalDateTime timestamp;
    private String type;
    private Long cumulativeQuantity;
}
