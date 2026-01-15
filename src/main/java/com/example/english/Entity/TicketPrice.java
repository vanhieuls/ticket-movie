package com.example.english.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TicketPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    BigDecimal price;
    String timeFrame;
    boolean specialDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_type_id")
    CinemaType cinemaType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_room_type_id")
    ScreenRoomType screenRoomType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_type_id")
    SeatType seatType;
}
