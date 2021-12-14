package com.example.demo.entities;

import com.example.demo.enums.TradeSide;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
/**
 Queue Order Log entity to receive data for queue.
 For persistence and for proper sequence of orders.
 **/
public class QueueOrderLog implements Serializable {
    private String id;
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
