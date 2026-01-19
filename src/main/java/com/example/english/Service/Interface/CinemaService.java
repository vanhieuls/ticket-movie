package com.example.english.Service.Interface;

import com.example.english.Dto.Request.CinemaRequest;
import com.example.english.Dto.Response.CinemaResponse;
import com.example.english.Dto.Response.CinemaSummaryResponse;
import com.example.english.Dto.Response.CinemaTypeResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CinemaService {
    CinemaResponse createCinema(@Valid @RequestBody CinemaRequest cinemaRequest);
    CinemaResponse updateCinema(Long id, CinemaRequest cinemaRequest);
    List<CinemaSummaryResponse> getCinemas(String address);
    CinemaResponse getCinema(Long id);
    Page<CinemaResponse> getListCinemas(Integer pageNumber, Integer sizeNumber);
    void deleteCinema(Long id);
    List<CinemaTypeResponse> getCinemaTypes();
}
