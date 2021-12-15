package com.example.demo.entities;

import com.example.demo.enums.Status;
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
    private long id;
    private String orderID;
    private long userID;
    private String productID;
    private int quantity;
    private double price;
    private TradeSide side;
    private LocalDateTime timestamp;
    private Status status;
    private long cumulativeQuantity;
    private boolean isSplitOrder;
    private double bestPrice;
    private int bestQuantity;
}
