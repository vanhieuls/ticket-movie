package com.example.english.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TxnInfo {
    @JsonProperty("showtime_id")
    private Long showTimeId;
    private List<Long> listSeatId;
}