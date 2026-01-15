package com.example.english.Controller.Admin;

import com.example.english.Dto.Request.MovieRequest;
import com.example.english.Dto.Response.MovieDetailResponse;
import com.example.english.Dto.Response.MovieSummaryResponse;
import com.example.english.Service.Interface.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    public MovieDetailResponse createMovie(@RequestBody MovieRequest movieRequest,@RequestParam MultipartFile imageFiles) {
        return movieService.createMovie(movieRequest, imageFiles);
    }

    @Operation(summary = "Cập nhật thông tin phim")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MovieDetailResponse updateMovie(@PathVariable Long id,@RequestBody MovieRequest movieRequest,@RequestParam MultipartFile imageFiles) {
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
    public List<MovieSummaryResponse> getAllMovies() {
        return movieService.getAllMovies();
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
}
