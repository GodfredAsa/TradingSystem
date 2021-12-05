package com.clientService.order.model;

import com.clientService.enums.OrderStatus;
import com.clientService.orderExecution.model.OrderExecution;
import com.clientService.user.model.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "orderModel")
public class OrderModel {

    @Id
    @Column(updatable = false, name = "orderModel_id")
    private String id;

    @Column(nullable = false)
    private int quantity;

    @Column(
            nullable = false
    )
    @NotEmpty
    @Positive
    @DecimalMin(value = "0.0", inclusive = false, message = "Please provide a price greater than zero")
    private double price;


    @Column(
            nullable = false,
            updatable = false
    )
    @NotEmpty
    @NotBlank
    private String side;


    @Column(
            length = 32,
            columnDefinition = "varchar(32) default 'PENDING'"
    )
    @Enumerated(
            EnumType.STRING
    )
    private OrderStatus status = OrderStatus.PENDING;


    @JoinColumn(name = "product_id")
    @ManyToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private Product product;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "appUser_id")
    @ToString.Exclude
    @JsonIgnore
    private AppUser userOrder;

    @OneToMany(mappedBy = "orderExecutions")
    @ToString.Exclude
    private List<OrderExecution> executions;

    public OrderModel(String id, int quantity, double price, String side,OrderStatus status, Product product, AppUser userOrder) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.side = side;
        this.product = product;
        this.userOrder = userOrder;
    }
}