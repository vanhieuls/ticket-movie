package com.example.english.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    Double priceFactor;
    boolean status;
    @OneToMany(mappedBy = "seatType", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    List<Seat> seats;
}
