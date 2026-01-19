package com.example.english.Dto.Chat;

import com.example.english.Dto.Response.ShowTimeDto;

import java.util.List;

public record ChatResponse(
        String type,
        String message,
        MovieDto movie,
        CinemaDto cinema,
        ScreenRoomDto screen,
        List<MovieDto> movies,
        List<CinemaDto> cinemas,
        List<ScreenRoomDto> screens,
        List<ScreenRoomTypeDto> types,
        List<ShowtimeDto> showtimes
) {
}
