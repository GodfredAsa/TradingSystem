package com.clientService.user.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "marketData")
public class MarketData {
    @Column(nullable = false)
    private double lastTradedPrice;

    @Column(nullable = false)
    private double sellLimit;

    @Column(nullable = false)
    private double bidPrice;

    @Column(nullable = false)
    private double askPrice;

    @Column(nullable = false)
    private int buyLimit;

    @Id
    private String ticker;

    @Column(nullable = false)
    private double maxPriceShift;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MarketData that = (MarketData) o;
        return ticker != null && Objects.equals(ticker, that.ticker);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}