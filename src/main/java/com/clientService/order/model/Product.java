package com.clientService.order.model;

import com.clientService.user.model.Portfolio;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name= "description", columnDefinition = "varchar(100)")
    private String description;

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    @JsonIgnore
    private List<OrderModel> orders;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinTable(name = "product_portfolio",
            joinColumns = @JoinColumn(name = "portfolio_ticker"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    @ToString.Exclude
    private List<Portfolio> portfolios;

    public Product(String ticker, String name, String description) {
        this.ticker = ticker;
        this.name = name;
        this.description = description;
    }



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
