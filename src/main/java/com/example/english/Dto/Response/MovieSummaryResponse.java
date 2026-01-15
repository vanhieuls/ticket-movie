package com.example.english.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieSummaryResponse {
    Long id;
    String name;
    String posterUrl;
    String duration;
    int ageLimit;
    String category;
    LocalDate releaseDate;
}
