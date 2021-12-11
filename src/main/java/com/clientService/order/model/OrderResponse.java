package com.clientService.order.model;

import com.clientService.orderExecution.model.OrderExecutionResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderResponse {

        /**
         * Model for orderResponse from market data
         */
        private String product;
        private int quantity;
        private double price;
        private String side;
        private List<OrderExecutionResponse> executions;
        private int cumulativeQuantity;

}
