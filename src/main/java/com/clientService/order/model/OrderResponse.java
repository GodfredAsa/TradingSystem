package com.clientService.order.model;

import com.clientService.order.service.OrderService;
import com.clientService.orderExecution.model.OrderExecutionResponse;
import com.clientService.user.model.MarketProduct;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Temporal;
import java.time.temporal.TemporalAmount;
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
