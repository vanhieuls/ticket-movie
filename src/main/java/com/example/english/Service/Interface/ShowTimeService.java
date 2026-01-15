package com.example.english.Service.Interface;

import com.example.english.Dto.Request.ShowTimeRequest;
import com.example.english.Dto.Response.CinemaShowTimeResponse;
import com.example.english.Dto.Response.MovieShowTimeResponse;
import com.example.english.Dto.Response.ShowTimeDto;
import com.example.english.Dto.Response.ShowTimeResponse;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public interface ShowTimeService {
    ShowTimeResponse createShowTime(ShowTimeRequest showTimeRequest);
    ShowTimeResponse updateShowTime(ShowTimeRequest showTimeRequest, Long id);
    ShowTimeResponse getShowTime(Long id);
    List<ShowTimeDto> getShowTimeList(Long screenRoomId, LocalDate date);
    List<MovieShowTimeResponse> getListShowTimeFilterCinema(Long cinemaId, LocalDate date);
    List<CinemaShowTimeResponse> getListShowTimeFilterMovie(Long movieId, LocalDate date, String address);
    List<ShowTimeDto> getShowTimeDto(String movieName, String cinemaName, LocalDate date);
    void deleteShowTime(Long id);
}
