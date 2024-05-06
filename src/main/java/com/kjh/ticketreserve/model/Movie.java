package com.kjh.ticketreserve.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Movie {

    @Id @GeneratedValue
    Long id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer price;
}
