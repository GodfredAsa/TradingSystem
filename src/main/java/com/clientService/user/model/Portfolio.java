package com.clientService.user.model;

import com.clientService.enums.PortfolioStatus;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "portfolio")
@Table
public class Portfolio {
    @Id
    @SequenceGenerator(
            name = "client_sequence",
            sequenceName = "client_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "client_sequence"
    )
    @Column(updatable = false)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PortfolioStatus status;
}
