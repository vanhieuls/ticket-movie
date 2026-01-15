package com.example.english.Dto.Request;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieRequest {
    String name;
    String description;
    String duration;
    String category;
    String country;
    String director;
    String actors;
    String posterUrl;
    int ageLimit;
    String trailerUrl;
    boolean status;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    LocalDate releaseDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    LocalDate endDate;

}
