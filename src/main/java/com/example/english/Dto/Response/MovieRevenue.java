package com.example.english.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MovieRevenue {
    String movieName;
    int totalTicket;
    BigDecimal totalRevenue;
}
