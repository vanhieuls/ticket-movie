package com.example.english.Dto.Response;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InvoiceSummary {
    private Long id;
    private String code;
    private String movieName;
    private String showTime;
    private String screenRoom;
    private BigDecimal totalMoney;
    private String createDate;
}
