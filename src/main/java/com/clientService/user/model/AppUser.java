package com.clientService.user.model;

import com.clientService.account.model.AccountModel;
import com.clientService.enums.UserRole;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "client")
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
    @Column(updatable = false, name = "user_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_account")
    private AccountModel account;

    @Column(nullable = false, columnDefinition = "varchar(100)")
    private String firstName;

    @Column(nullable = false, columnDefinition = "varchar(100)")
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "varchar(10)")
    private String contact;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Portfolio> portfolios;


    public AppUser(String firstName, String lastName, LocalDate dateOfBirth, String email, String password, String contact, UserRole userRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.password = password;
        this.contact = contact;
        this.userRole = userRole;
        this.account = account;
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
