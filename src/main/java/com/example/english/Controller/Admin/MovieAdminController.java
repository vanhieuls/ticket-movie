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
@Tag(name = "Movie Admin Controller", description = "APIs quản lý phim cho admin")
public class MovieAdminController {
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

}
