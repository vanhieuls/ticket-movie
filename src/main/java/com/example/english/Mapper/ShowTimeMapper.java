package com.example.english.Mapper;

import com.example.english.Dto.Request.ShowTimeRequest;
import com.example.english.Dto.Response.ShowTimeDetail;
import com.example.english.Dto.Response.ShowTimeDto;
import com.example.english.Dto.Response.ShowTimeResponse;
import com.example.english.Dto.Response.ShowTimeSummaryResponse;
import com.example.english.Entity.ShowTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShowTimeMapper {
    ShowTime toShowTimeEntity(ShowTimeRequest showTimeRequest);
    @Mapping(source = "screenRoom.id", target = "screenRoomId")
    @Mapping(source = "movie.id", target = "movieId")
    ShowTimeResponse toShowTimeResponse(ShowTime showTime);
    @Mapping(source = "movie.name", target = "movieName")
    @Mapping(source = "screenRoom.cinema.name", target = "cinemaName")
    @Mapping(source = "screenRoom.name", target = "screenName")
    ShowTimeDetail toShowTimeDetail(ShowTime showTime);
    @Mapping(source = "screenRoom.id", target = "screenRoomId")
    @Mapping(source = "movie.name", target = "movieName")
    ShowTimeDto toShowTimeDto(ShowTime showTime);
    ShowTimeSummaryResponse toShowTimeSummaryResponse(ShowTime showTime);
    void updateShowTimeFromRequest(ShowTimeRequest showTimeRequest, @MappingTarget ShowTime showTime);
    List<ShowTimeDto> toShowTimeDtoList(List<ShowTime> showTimes);
}
