package com.example.english.Controller.Admin;

import com.example.english.Dto.Request.FilterMovie;
import com.example.english.Dto.Request.MovieRequest;
import com.example.english.Dto.Response.MovieDetailResponse;
import com.example.english.Dto.Response.MovieResponse;
import com.example.english.Dto.Response.MovieSummaryResponse;
import com.example.english.Service.Interface.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
@RestController
@RequestMapping("/admin/movies")
@Tag(name = "Movie Controller", description = "APIs quản lý phim cho admin")
public class MovieController {
    MovieService movieService;

    @Operation(summary = "Tạo mới phim")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MovieDetailResponse createMovie(@ModelAttribute MovieRequest movieRequest,
                                           @RequestParam("poster") MultipartFile poster) {
        return movieService.createMovie(movieRequest, poster);
    }

    @Operation(summary = "Cập nhật thông tin phim")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MovieDetailResponse updateMovie(@PathVariable Long id,
                                           @Valid @ModelAttribute MovieRequest movieRequest,
                                           @RequestParam(value = "imageFiles", required = false) MultipartFile imageFiles) {
        return movieService.updateMovie(id, movieRequest, imageFiles);
    }

    @Operation(summary = "Xoá phim (chuyển trạng thái về không hoạt động)")
    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }

    @Operation(summary = "Lấy chi tiết phim theo ID")
    @GetMapping("/{id}")
    public MovieDetailResponse getMovie(@PathVariable Long id) {
        return movieService.getMovie(id);
    }

    @Operation(summary = "Cập nhật trạng thái hoạt động của phim")
    @PutMapping("/status-active/{id}")
    public void updateMovieActive(@PathVariable Long id) {
        movieService.updateMovieActive(id);
    }

    @Operation(summary = "Lấy danh sách tất cả phim")
    @GetMapping
    public Page<MovieSummaryResponse> getAllMovies(@RequestParam Integer pageNumber, @RequestParam Integer pageSize) {
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
    @GetMapping("/cinema-address")
    public List<String> getListCinemaAddress(@RequestBody FilterMovie filterMovie) {
        return movieService.getListCinemaAddress(filterMovie);
    }

    @Operation(summary = "Lấy danh sách phim theo bộ lọc")
    @GetMapping("/filter")
    public Page<MovieSummaryResponse> getFilterMovie(@RequestParam(defaultValue = "0") Integer pageNumber, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(required = false) String category, @RequestParam(required = false) String brand,
                                                     @RequestParam(required = false) String properties, @RequestParam(required = false) String sortDir,@RequestParam(required = false) BigDecimal minPrice,@RequestParam(required = false) BigDecimal maxPrice) {
       return movieService.getFilterMovie(pageNumber, pageSize, category, brand, properties, sortDir, minPrice, maxPrice);
    }

    @Operation(summary = "Lấy danh sách phim theo ngày chiếu")
    @GetMapping("/movie-show-day")
    public List<MovieResponse> getMovieShowDay(LocalDate date) {
        return movieService.getMovieShowDay(date);
    }
}
