package com.clientService.order.model;

import com.clientService.orderExecution.model.OrderExecutionResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderReportModel {

    private String orderID;
    private long userID;
    private String productID;
    private int quantity;
    private double price;
    private String side;
    private LocalDateTime timestamp;
    private String status;
    private int cumulativeQuantity;
    private boolean isSplitOrder;
    private double bestPrice;
    private int bestQuantity;

}
