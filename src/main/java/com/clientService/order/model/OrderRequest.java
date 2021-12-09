package com.clientService.order.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequest {


    /**
     * Model for client order Request
     */
    private String product;
    private int quantity;
    private double price;
    private String side;
    private Long portfolioId;

}
