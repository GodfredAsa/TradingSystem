package com.clientService.user.model;

import com.clientService.order.model.OrderModel;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "ticker", columnDefinition = "varchar(50)")
    private String ticker;

    @Column(name = "name", columnDefinition = "varchar(50)")
    private String name;

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<OrderModel> orderModels;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "product_portfolio",
    joinColumns = @JoinColumn(name = "portfolio_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    @ToString.Exclude
    private List<Portfolio> portfolios;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Product product = (Product) o;
        return ticker != null && Objects.equals(ticker, product.ticker);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
