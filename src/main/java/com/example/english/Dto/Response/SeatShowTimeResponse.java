package com.example.english.Dto.Response;

import com.example.english.Enum.SeatStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatShowTimeResponse {
    Long id;
    String seatCode;
    BigDecimal price;
    String status = SeatStatus.AVAILABLE;
    String seatTypeName;
}
