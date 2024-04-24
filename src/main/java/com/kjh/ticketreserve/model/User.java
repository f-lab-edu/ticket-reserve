package com.kjh.ticketreserve.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
    name = "users",
    uniqueConstraints = @UniqueConstraint(columnNames = { "email" }))
@NoArgsConstructor
public class User {

    @Id @GeneratedValue
    Long id;
    private String email;
    @Column(length = 1000)
    private String passwordHash;
}