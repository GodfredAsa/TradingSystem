package com.example.demo.entities;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MarketDataReport")
public class MarketDataLog {
    @Id
    @SequenceGenerator(name = "MarketDataIDSequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO)

    @Column(name = "lastTradedPrice",nullable = false)
    private String lastTradedPrice;
    @Column(name = "sellLimit",nullable = false)
    private double sellLimit;
    @Column(name = "bidPrice",nullable = false)
    private double bidPrice;
    @Column(name = "askPrice",nullable = false)
    private int askPrice;
    @Column(name = "buyLimit",nullable = false)
    private String buyLimit;
    @Column(name = "ticker",nullable = false)
    private String ticker;
    @Column(name = "maxPriceShift",nullable = false)
    private int maxPriceShift;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MarketDataLog that = (MarketDataLog) o;
        return lastTradedPrice != null && Objects.equals(lastTradedPrice, that.lastTradedPrice);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
