package com.example.english.Dto.Response;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieDetailResponse {
    Long id;
    String name;
    String description;
    int duration;
    String category;
    String country;
    String director;
    String actors;
    String posterUrl;
    int ageLimit;
    String trailerUrl;
    boolean status;
    LocalDate releaseDate;
    LocalDate endDate;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
