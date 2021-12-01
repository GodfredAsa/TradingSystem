package com.clientService.account.model;

import com.clientService.user.model.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(
        name = "account"
)
public class AccountModel {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            name = "account_id"
    )
    private Integer id;

    @OneToOne(mappedBy = "account")
    private AppUser user;

    @Column(nullable = false, unique = true)
    private double balance;

    public AccountModel(AppUser user, double balance) {
        this.user = user;
        this.balance = balance;
    }
}
