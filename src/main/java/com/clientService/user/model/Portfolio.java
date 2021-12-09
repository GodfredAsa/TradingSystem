package com.clientService.user.model;

import com.clientService.enums.PortfolioStatus;
import com.clientService.order.model.OrderModel;
import com.clientService.order.model.Product;
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
@Table(name = "portfolio")
public class Portfolio {
    @Id
    @SequenceGenerator(
            name = "portfolio_sequence",
            initialValue = 1,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    @Column(updatable = false)
    private Long id;

    @JoinColumn(name = "appUser_id")
    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    private AppUser userPortfolio;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PortfolioStatus status;

    @ManyToMany(mappedBy = "portfolios")
    @JsonIgnore
    @ToString.Exclude
    private List<Product> products;

    @OneToMany(mappedBy = "OrderPortfolio")
    @Column(nullable = false)
    private List<OrderModel> orders;

    public Portfolio(AppUser appUser, PortfolioStatus status) {
        this.userPortfolio = appUser;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Portfolio portfolio = (Portfolio) o;
        return id != null && Objects.equals(id, portfolio.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
