package com.example.english.Controller;

import com.example.english.Dto.Request.FilterMovie;
import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.MovieResponse;
import com.example.english.Dto.Response.MovieSummaryResponse;
import com.example.english.Service.Interface.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/movie")
@Slf4j
@Tag(name = "Movie Controller", description = "APIs for movie functionalities")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieController {
    MovieService movieService;

    @Operation(summary = "Lấy danh sách tất cả phim")
    @GetMapping
    public Page<MovieSummaryResponse> getAllMovies(@RequestParam(required = false, defaultValue = "0") Integer pageNumber, @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return movieService.getAllMovies(pageNumber, pageSize);
    }

    @Operation(summary = "Lấy danh sách phim đang chiếu")
    @GetMapping("/now-playing")
    public List<MovieSummaryResponse> getNowPlayingMovies() {
        return movieService.getNowPlayingMovies();
    }

    @Operation(summary = "Lấy danh sách phim sắp chiếu")
    @GetMapping("/upcoming")
    public List<MovieSummaryResponse> getUpcomingMovies() {
        return movieService.getUpcomingMovies();
    }

    @Operation(summary = "Lấy danh sách địa chỉ rạp chiếu theo bộ lọc")
    @PostMapping("/address")
    public ApiResponse<List<String>> getListCinemaAddress(@RequestBody FilterMovie filterMovie) {
        return ApiResponse.<List<String>>builder()
                .code(200)
                .message("Get cinema address list successfully")
                .result(movieService.getListCinemaAddress(filterMovie))
                .build();
    }

    @Operation(summary = "Lấy danh sách phim theo bộ lọc")
    @GetMapping("/filter")
    public Page<MovieSummaryResponse> getFilterMovie(@RequestParam(defaultValue = "0") Integer pageNumber, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(required = false) String category, @RequestParam(required = false) String brand,
                                                     @RequestParam(required = false) String properties, @RequestParam(required = false) String sortDir, @RequestParam(required = false) BigDecimal minPrice, @RequestParam(required = false) BigDecimal maxPrice) {
        return movieService.getFilterMovie(pageNumber, pageSize, category, brand, properties, sortDir, minPrice, maxPrice);
    }

    @Operation(summary = "Lấy danh sách phim theo ngày chiếu")
    @GetMapping("/movie-show-day")
    public List<MovieResponse> getMovieShowDay(LocalDate date) {
        return movieService.getMovieShowDay(date);
    }
}
