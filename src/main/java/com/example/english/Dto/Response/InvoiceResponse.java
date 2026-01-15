package com.example.english.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponse {
    Long invoiceId;
    String movieName;
    int totalTicket;
    LocalDate showDate;
    LocalTime startTime;
    BigDecimal totalMoney;
}
