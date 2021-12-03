package com.clientService.user.model;

import com.clientService.enums.UserRole;
import com.clientService.order.model.Order;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "appUser")
public class AppUser {

    /**
     * User Model with AllArgsConstructor and constructor without id
     */

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
    @Column(updatable = false, name = "appUser_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(100)")
    private String firstName;

    @Column(nullable = false, columnDefinition = "varchar(100)")
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToOne(mappedBy = "userAccount")
    @ToString.Exclude
    private Account account;

    @OneToMany(mappedBy = "userPortfolio")
    @ToString.Exclude
    private List<Portfolio> portfolios;

    @OneToMany(mappedBy = "userOrder")
    @ToString.Exclude
    private List<Order> orders;

    public AppUser(String firstName, String lastName, String email, String password, UserRole userRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AppUser client = (AppUser) o;
        return id != null && Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
