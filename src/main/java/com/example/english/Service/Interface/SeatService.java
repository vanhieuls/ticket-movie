package com.example.english.Service.Interface;

import com.example.english.Dto.Request.SeatRequest;
import com.example.english.Dto.Response.SeatResponse;
import com.example.english.Dto.Response.SeatShowTime;
import com.example.english.Dto.Response.SeatSummaryRepo;

import java.util.List;

public interface SeatService {
    List<SeatResponse> createSeats(SeatRequest seatRequest);
    SeatShowTime getListSeat(Long showTimeId);
    List<SeatSummaryRepo> getSeatListByScreenRoom(long showRoomId);
    void updateActiveSeat(Long id);
}
