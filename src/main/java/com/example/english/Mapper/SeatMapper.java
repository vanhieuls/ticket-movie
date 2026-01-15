package com.example.english.Mapper;

import com.example.english.Dto.Response.SeatResponse;
import com.example.english.Dto.Response.SeatShowTimeResponse;
import com.example.english.Dto.Response.SeatSummaryRepo;
import com.example.english.Entity.Seat;
import com.example.english.Enum.SeatStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    @Mapping(target = "seatTypeName", source = "seatType.name")
    @Mapping(target = "seatTypeId", source = "seatType.id")
    SeatSummaryRepo toSeatSummaryRepo (Seat seat);
    List<SeatSummaryRepo> toSeatSummaryRepoList (List<Seat> seats);

    @Mapping(target = "seatTypeId", source = "seatType.id")
    @Mapping(target = "screenRoomId", source ="screenRoom.id")
    SeatResponse toSeatResponse(Seat seat);
    List<SeatResponse> toSeatResponseList (List<Seat> seats);

    @Mapping(target = "seatTypeName", source = "seatType.name")
    @Mapping(target = "status", constant = "AVAILABLE")
    SeatShowTimeResponse toSeatShowTimeResponse(Seat seat);
}
