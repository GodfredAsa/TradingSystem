package com.clientService.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "account")
public class Account implements Serializable {
    @Id
    @SequenceGenerator(
            name = "account_sequence",
            initialValue = 1,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    @Column(updatable = false)
    private Long id;

    @Column(name = "balance", nullable = false)
    private Double balance;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "appUser_id")
    @JsonIgnore
    @ToString.Exclude
    private AppUser userAccount;

    public Account(double balance, AppUser user) {
        this.balance = balance;
        this.userAccount = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Account account = (Account) o;
        return id != null && Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
