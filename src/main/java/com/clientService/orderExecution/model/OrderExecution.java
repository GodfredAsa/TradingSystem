package com.clientService.orderExecution.model;

import com.clientService.enums.Exchange;
import com.clientService.order.model.OrderModel;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "order_execution")
public class OrderExecution {
    @Id
    @SequenceGenerator(
            name = "ExcExe_sequence",
            sequenceName = "ExcExe_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ExcExe_sequence"
    )
    @Column(
            name = "execution_id",
            updatable = false
    )
    private Long execution_id;

    @JoinColumn(
            name = "orderModel_id"
    )
    @ManyToOne(
            cascade = CascadeType.ALL
    )
<<<<<<< HEAD
    private OrderModel orderExecutions;
=======
    private OrderModel order;
>>>>>>> 078426a9cc049cb8714af2ecd352ccec7903cae1

    @Column(
            nullable = false
    )
    private double price;

    @Column(
            nullable = false
    )
    @Enumerated(
            EnumType.STRING
    )
    private Exchange exchange;

    @Column(
            nullable = false,
            updatable = false
    )
    private String timeStamp;

    private int cumulativeQuantity;

}
