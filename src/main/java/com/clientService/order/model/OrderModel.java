package com.clientService.order.model;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table
public class OrderModel {

    @Id
    @SequenceGenerator(
            name = "order_sequence",
            sequenceName = "order_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_sequence"
    )
    @Column(
            updatable = false
    )
    private Long id;



    @Column(
            nullable = false,
            unique = true,
            updatable = false
    )
    private String user_id;

    @Column(
            nullable = false,
            unique = true,
            updatable = false
    )
    private String product_id;


//
//    @Column(
//            nullable = false,
//            length = 50
//    )
//    private String lastName;
//
//    @Column(
//            nullable = false
//    )
//    private LocalDate dateOfBirth;
//
//    @Column(
//            nullable = false,
//            unique = true
//    )
//    private String email;
//
//    @Column(
//            nullable = false
//    )
//    private String password;
//
//    @Column(
//            nullable = false,
//            length = 10
//    )
//    private String contact;
}
