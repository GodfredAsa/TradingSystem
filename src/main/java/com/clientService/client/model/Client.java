package com.clientService.client.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(
        name = "client"
)
public class Client {
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
    @Column(
            updatable = false
    )
    private Long id;

    @Column(
            nullable = false,
            length = 50
    )
    private String firstName;

    @Column(
            nullable = false,
            length = 50
    )
    private String lastName;

    @Column(
            nullable = false
    )
    private LocalDate dateOfBirth;

    @Column(
            nullable = false,
            unique = true
    )
    private String email;

    @Column(
            nullable = false
    )
    private String password;

    @Column(
            nullable = false,
            length = 10
    )
    private String contact;


    public Client(String firstName, String lastName, LocalDate dateOfBirth, String email, String password, String contact) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.password = password;
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Client client = (Client) o;
        return id != null && Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
