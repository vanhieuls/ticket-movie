package com.example.english.Controller.Admin;
import com.example.english.Dto.Request.MovieRequest;
import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.MovieDetailResponse;
import com.example.english.Service.Interface.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ApiResponse<MovieDetailResponse> createMovie(@ModelAttribute MovieRequest movieRequest,
                                                       @RequestParam("poster") MultipartFile poster) {
        return ApiResponse.<MovieDetailResponse>builder()
                .code(200)
                .message("Tạo phim thành công")
                .result(movieService.createMovie(movieRequest, poster))
                .build();
    }

    @Operation(summary = "Cập nhật thông tin phim")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MovieDetailResponse> updateMovie(@PathVariable Long id,
                                           @Valid @ModelAttribute MovieRequest movieRequest,
                                           @RequestParam(value = "imageFiles", required = false) MultipartFile imageFiles) {
        return ApiResponse.<MovieDetailResponse>builder()
                .code(200)
                .message("Cập nhật phim thành công")
                .result(movieService.updateMovie(id, movieRequest, imageFiles))
                .build();
    }

    @Operation(summary = "Xoá phim (chuyển trạng thái về không hoạt động)")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Xoá phim thành công")
                .build();
    }

    @Operation(summary = "Lấy chi tiết phim theo ID")
    @GetMapping("/{id}")
    public ApiResponse<MovieDetailResponse> getMovie(@PathVariable Long id) {
        return ApiResponse.<MovieDetailResponse>builder()
                .code(200)
                .message("Lấy chi tiết phim thành công")
                .result(movieService.getMovie(id))
                .build();
    }

    @Operation(summary = "Cập nhật trạng thái hoạt động của phim")
    @PutMapping("/status-active/{id}")
    public ApiResponse<Void> updateMovieActive(@PathVariable Long id) {
        movieService.updateMovieActive(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Cập nhật trạng thái hoạt động của phim thành công")
                .build();
    }

}
