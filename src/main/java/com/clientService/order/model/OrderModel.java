package com.clientService.order.model;

import com.clientService.orderExecution.model.OrderExecutionModule;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "order")
public class OrderModel {

    @Id
    @SequenceGenerator(
            name = "order_sequence",
            sequenceName = "order_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_sequence"
    )
    @Column(
            updatable = false,
            name = "order_id"
    )
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(
            name = "product_id"
    )
    private long product_id;

    @Column(
            nullable = false
    )
    private int ord_quantity;

    @Column(
            nullable = false
    )
    private double ord_price;

    @Column(
            nullable = false,
            updatable = false
    )
    private String ord_side;

    @Column(
            nullable = false
    )
    private boolean ord_status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(
            nullable = false,
            updatable = false
    )
    private Date ord_timeStamp;

    @PrePersist
    public void onCreate() {
        ord_timeStamp = new Date();
    }

    @ManyToOne(name = "product_id")
    private Product product;

    @OneToMany(
            mappedBy = "order"
    )
    private List<OrderExecutionModule> order_executions;

    @OneToOne(
            cascade = CascadeType.ALL,
            optional = false
    )
    @JoinColumn(
            name = "user_id"
    )
    private long user_id;

}
