package com.example.english.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
public class Movie extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    @Lob
    @Column(columnDefinition = "TEXT")
    String description;
    String duration;
    String category;
    String country;
    String director;
    String actors;
    String posterUrl;
    int ageLimit;
    @Column(length = 500)
    String trailerUrl;
    boolean status;
    LocalDate releaseDate;
    LocalDate endDate;


}
