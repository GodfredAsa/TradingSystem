package com.example.demo.entities;

import com.example.demo.enums.Status;
import com.example.demo.enums.TradeSide;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "OrderLog")
public class OrderLog {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "OrderLog", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "userID",nullable = false)
    private Long userID;
    @Column(name = "exchangeOrderID",nullable = false)
    private String exchangeOrderID;
    @Column(name = "productID",nullable = false)
    private int productID;
    @Column(name = "quantity",nullable = false)
    private int quantity;
    @Column(name = "price",nullable = false)
    private Double price;
    @Column(name = "side",nullable = false)
    @Enumerated(EnumType.STRING)
    private TradeSide side;
    @Column(name = "timestamp",nullable = false)
    private LocalDateTime timestamp;
    @Column(name = "status",nullable = false)
    private Status status;
    @Column(name = "type",nullable = false)
    private String type;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OrderLog orderLog = (OrderLog) o;
        return id != null && Objects.equals(id, orderLog.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
