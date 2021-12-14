package com.example.demo.entities;

import com.example.demo.enums.Status;
import com.example.demo.enums.TradeSide;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

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
@JsonSerialize

/**
 Order Log entity to receive data from the order service
 and store the data onto the database.
 **/
public class OrderLog {
    @Id
    @Column(name = "orderID", nullable = false)
    private String id;
    @Column(name = "userID", nullable = false)
    private long userID;
    @Column(name = "productIDd", nullable = false)
    private String productID;
    @Column(name = "quantity", nullable = false)
    private int quantity;
    @Column(name = "price", nullable = false)
    private double price;
    @Column(name = "side", nullable = false)
    @Enumerated(EnumType.STRING)
    private TradeSide side;
    @Column(name = "timestamp", nullable = false)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timestamp;
    @Column(name = "status", nullable = false)
    private Status status;
    @Column(name = "cumulativeQuantity", nullable = false)
    private int cumulativeQuantity;
    @Column(name = "isSplitOrder", nullable = false)
    private boolean isSplitOrder;
    @Column(name = "bestPrice", nullable = false)
    private double bestPrice;
    @Column(name = "bestQuantity", nullable = false)
    private int bestQuantity;


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
