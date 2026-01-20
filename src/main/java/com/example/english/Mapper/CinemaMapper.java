package com.example.english.Mapper;

import com.example.english.Dto.Request.CinemaRequest;
import com.example.english.Dto.Response.CinemaResponse;
import com.example.english.Dto.Response.CinemaSummaryResponse;
import com.example.english.Dto.Response.CinemaTypeResponse;
import com.example.english.Entity.Cinema;
import com.example.english.Entity.CinemaType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CinemaMapper {
    Cinema toCinema(CinemaRequest cinema);
    @Mapping(target = "cinemaTypeName", source = "cinemaType.name")
    CinemaResponse toCinemaResponse(Cinema cinema);
    @Mapping(target = "name", source = "cinemaType.name")
    @Mapping(target = "id", source = "cinemaType.id")
    CinemaTypeResponse toCinemaTypeResponse(Cinema cinema);
    List<CinemaTypeResponse> toCinemaTypeResponses(List<Cinema> cinemas);
    CinemaSummaryResponse toCinemaSummaryResponse(Cinema cinema);
    List<CinemaSummaryResponse> toCinemaSummaryResponses(List<Cinema> cinemas);
    void updateCinemaFromRequest(CinemaRequest cinemaRequest, @MappingTarget Cinema cinema);
    List<CinemaResponse> toCinemaResponses(List<Cinema> cinemas);
    CinemaTypeResponse toCinemaTypeResponseFromCinemaType(CinemaType cinemaType);
    List<CinemaTypeResponse> toCinemaTypeResponsesFromCinemaTypes(List<CinemaType> cinemaTypes);
}
