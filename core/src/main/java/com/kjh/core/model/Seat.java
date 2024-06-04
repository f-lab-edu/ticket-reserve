package com.kjh.core.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "theater_id", "row_code", "number" }))
@NoArgsConstructor
public class Seat {

    @Id @GeneratedValue
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

    @Column(columnDefinition = "char(1)")
    @Enumerated(EnumType.STRING)
    private SeatRowCode rowCode;

    private Integer number;

}
