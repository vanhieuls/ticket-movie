package com.example.english.Dto.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieRequest {
    String name;
    String description;
    int duration;
    String category;
    String country;
    String director;
    String actors;
    int ageLimit;
    String trailerUrl;
    boolean status;
//    @DateTimeFormat(pattern = "dd/MM/yyyy")
    LocalDate releaseDate;
//    @DateTimeFormat(pattern = "dd/MM/yyyy")
    LocalDate endDate;

}
