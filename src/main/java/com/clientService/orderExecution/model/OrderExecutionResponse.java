package com.clientService.orderExecution.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderExecutionResponse {

    /**
     * Model to hold order executions response
     */
    private String timestamp;
    private double price;
    private int quantity;

}
