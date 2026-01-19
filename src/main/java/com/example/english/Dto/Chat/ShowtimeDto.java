package com.example.english.Dto.Chat;

public record ShowtimeDto(
        Long id,
        String movieName,
        String cinemaName,
        String screenName,
        String showDate,
        String startTime,
        String endTime
) {}