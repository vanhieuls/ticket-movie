package com.example.english.Dto.Response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InvoiceDetailResponse {
    private Long invoiceId;
    private String movieName;
    private String screenRoomType;
    private String bookingCode;
    private LocalDate showDate;
    private LocalTime startTime;
    private String screenRoomName;
    private List<String> seatList;
    private int totalTicket;
    private BigDecimal totalMoney;
    private Long userId;
    private String userName;
}
