package com.example.english.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScreenRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    Boolean status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="Cinema_id")
    Cinema cinema;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ScreenRoomType_id")
    ScreenRoomType screenRoomType;
    @OneToMany(mappedBy = "screenRoom", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    Set<Seat> seats;
}
