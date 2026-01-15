package com.example.english.Dto.Response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class InvoiceDetailAD {
    private Long invoiceId;
    private String bookingCode;
    private String movieName;
    private String showTime;
    private String showDate;
    private String screenRoomTypeName;
    private String screenRoomName;
    private String cinema;
    private String bookDay;

    private Long userId;
    private String userName;
    private String userEmail;
    private String userPhone;
    private String status;
    private BigDecimal totalMoney;

    private List<SeatInvoice> seatList;

    @Data
    public static class SeatInvoice{
        private Long id;
        private String seatCode;
        private BigDecimal price;
    }
}
