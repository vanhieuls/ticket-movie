package com.example.english.Dto.Response;

public record ShowTimeDetail(
    Long id,
    String movieName,
    String cinemaName,
    String screenName,
    String showDate,
    String startTime,
    String endTime
){}
