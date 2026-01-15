package com.example.english.Dto.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class InvoiceRequest {
    @JsonProperty("showtime_id")
    private Long showtimeId;
    private List<Long> listSeatId;
    private String vnp_TxnRef;
    private String vnp_Amount;
}
