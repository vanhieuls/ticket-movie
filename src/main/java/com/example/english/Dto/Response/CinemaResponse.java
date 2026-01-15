package com.example.english.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class CinemaResponse {
    Long id;
    String name;
    String address;
    Boolean status;
    String cinemaTypeName;
}
