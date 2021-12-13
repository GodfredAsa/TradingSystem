package com.example.demo.entities;

import com.example.demo.enums.AuthStatus;
import com.example.demo.enums.UserRole;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "AuthenticationLog")
public class AuthenticationLog {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "authIDSequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = " userID", nullable = false)
    private Long userID;

    @Column(name = "authStatus", nullable = false, columnDefinition = "varchar(10)")
    @Enumerated(EnumType.STRING)
    private AuthStatus authStatus;

    @Column(name = "role", nullable = false, columnDefinition = "varchar(10)")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "date", nullable = false)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime localDateTime;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AuthenticationLog that = (AuthenticationLog) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
