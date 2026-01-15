package com.example.english.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class CinemaSummaryResponse {
    private Long id;
    private String name;
}
