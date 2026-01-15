package com.example.english.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "row_seat")  // tránh từ khóa ROW
    int row;
    @Column(name = "column_seat")  // tránh từ khóa COLUMN
    int column;
    boolean isActive;
    String seatCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SeatType_id")
    SeatType seatType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ScreenRoom_id")
    ScreenRoom screenRoom;

}

