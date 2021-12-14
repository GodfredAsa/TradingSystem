package com.clientService.user.model;

import com.clientService.order.model.Product;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Table(name = "portfolio_product")
public class PortfolioProductData implements Serializable {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            initialValue = 1,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    private int quantity = 0;


    public PortfolioProductData(Product product, Portfolio portfolio, int quantity) {
        this.product = product;
        this.portfolio = portfolio;
        this.quantity = quantity;
    }
}
