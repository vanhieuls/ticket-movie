package com.example.english.Controller;

import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.CinemaShowTimeResponse;
import com.example.english.Dto.Response.ShowTimeDto;
import com.example.english.Service.Interface.ShowTimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/showtime")
@Slf4j
@Tag(name = "ShowTime Controller", description = "APIs for showtime functionalities")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShowTimeController {
    ShowTimeService showTimeService;

    @GetMapping("/filter")
    @Operation(summary = "Get Show Time List by Movie and Cinema", description = "API lấy danh sách suất chiếu theo tên phim, tên rạp và ngày")
    public ApiResponse<List<ShowTimeDto>> getShowTimeDto(@RequestParam("movieName") String movieName,
                                                         @RequestParam("cinemaName") String cinemaName,
                                                         @RequestParam("date") LocalDate date) {
        return ApiResponse.<List<ShowTimeDto>>builder()
                .code(200)
                .message("Get show time list by movie and cinema successfully")
                .result(showTimeService.getShowTimeDto(movieName, cinemaName, date))
                .build();
    }
    @GetMapping("/movie")
    @Operation(summary = "Get Show Time List by Movie", description = "API lấy danh sách suất chiếu theo phim, địa chỉ rạp và ngày")
    public ApiResponse<List<CinemaShowTimeResponse>> getShowTimeListByMovie(@RequestParam("movie_id") Long movieId,
                                                                            @RequestParam("date") LocalDate date,
                                                                            @RequestParam("address") String address) {
        return ApiResponse.<List<CinemaShowTimeResponse>>builder()
                .code(200)
                .message("Get show time list by movie successfully")
                .result(showTimeService.getListShowTimeFilterMovie(movieId, date, address))
                .build();
    }

}
