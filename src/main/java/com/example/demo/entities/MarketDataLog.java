package com.example.demo.entities;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@Table(name = "MarketDataLog")
@NoArgsConstructor
public class MarketDataLog {
    @Id
    @SequenceGenerator(name = "MarketDataIDSequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "exchangeID",nullable = false)
    private Long exchangeID;
    @Column(name = "ticker", nullable = false)
    private String ticker;
    @Column(name = "price", nullable = false)
    private Double price;
    @Column(name = " timestamp",nullable = false )
    private LocalDateTime timestamp;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MarketDataLog that = (MarketDataLog) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
