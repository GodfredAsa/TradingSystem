package com.clientService.order.model;

import com.clientService.enums.OrderStatus;
import com.clientService.orderExecution.model.OrderExecution;
import com.clientService.user.model.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "order")
public class Order {


    @Id
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
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @JsonIgnore
    private AppUser userOrder;

    @OneToMany(mappedBy = "order")
    @ToString.Exclude
    private List<OrderExecution> executions;

}
