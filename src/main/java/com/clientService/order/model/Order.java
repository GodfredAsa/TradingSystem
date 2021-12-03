package com.clientService.order.model;

import com.clientService.user.model.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table
public class Order {
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
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false)
    private int ord_quantity;

    @Column(nullable = false)
    private double ord_price;

    @Column(nullable = false, updatable = false)
    private String ord_side;

    @Column(nullable = false)
    private boolean ord_status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date ord_timeStamp;

    @PrePersist
    public void onCreate() {
        ord_timeStamp = new Date();
    }

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


}