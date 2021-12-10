//package com.clientService.order.model;
//
//import com.clientService.user.model.AppUser;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import lombok.*;
//
//import javax.persistence.*;
//import java.util.Date;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
//@Getter
//@Setter
//@Entity
//@Table
//public class Order {
//    @Id
//    @SequenceGenerator(
//            name = "order_sequence",
//            sequenceName = "order_sequence",
//            allocationSize = 1
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "order_sequence"
//    )
//    @Column(updatable = false)
//    private Long id;
//
//    @Column(nullable = false)
//    private int ord_quantity;
//
//    @Column(nullable = false)
//    private double ord_price;
//
//    @Column(nullable = false, updatable = false)
//    private String ord_side;
//
//    @Column(nullable = false)
//    private boolean ord_status;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(nullable = false, updatable = false)
//    private Date ord_timeStamp;
//
//    @PrePersist
//    public void onCreate() {
//        ord_timeStamp = new Date();
//    }
//
//    @JoinColumn(name = "product_id")
//    @ManyToOne(cascade = CascadeType.ALL)
//    @ToString.Exclude
//    @JsonIgnore
//    private Product product;
//
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id")
//    @ToString.Exclude
//    @JsonIgnore
//    private AppUser userOrder;
//
//
//}

package com.clientService.order.model;

import com.clientService.enums.OrderStatus;
import com.clientService.orderExecution.model.OrderExecution;
import com.clientService.user.model.AppUser;
import com.clientService.user.model.Portfolio;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "order_model")
public class OrderModel {

    @Id
    @Column(updatable = false)
    private String id;


    @Min(0)
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "portfolio_id")
    @JsonIgnore
    private Portfolio OrderPortfolio;


    @OneToMany(mappedBy = "orderExecutions")
    @ToString.Exclude
    private List<OrderExecution> executions;

    @Column(
            name = "cumulative_quantity",
            nullable = false
    )
    @NotEmpty
    @NotBlank
    private int cumulativeQuantity;

    public OrderModel(String id, int quantity, double price, String side, Product product, Portfolio orderPortfolio, AppUser userOrder,List<OrderExecution> orderExecutions,  int cumulativeQuantity) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.side = side;
        this.product = product;
        this.OrderPortfolio = orderPortfolio;
        this.userOrder = userOrder;
        this.status = OrderStatus.PENDING;
        this.executions = orderExecutions;
        this.cumulativeQuantity = cumulativeQuantity;
    }
}