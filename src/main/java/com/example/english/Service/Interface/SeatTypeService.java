package com.example.english.Service.Interface;

import com.example.english.Dto.Request.SeatTypeRequest;
import com.example.english.Dto.Response.SeatTypeResponse;

import java.util.List;

public interface SeatTypeService {
    SeatTypeResponse createSeatType(SeatTypeRequest seatTypeRequest);
    SeatTypeResponse updateSeatType(Long id, SeatTypeRequest seatTypeRequest);
    SeatTypeResponse getSeatType(Long id);
    void updateStatusSeatType(Long id);
    List<SeatTypeResponse> getAllSeatType();
}
