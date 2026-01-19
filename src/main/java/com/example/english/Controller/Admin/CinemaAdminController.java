package com.example.english.Controller.Admin;

import com.example.english.Dto.Request.CinemaRequest;
import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.CinemaResponse;
import com.example.english.Dto.Response.CinemaSummaryResponse;
import com.example.english.Dto.Response.CinemaTypeResponse;
import com.example.english.Service.Interface.CinemaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/cinemas")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Cinema Admin Controller", description = "APIs quản lý rạp chiếu phim cho admin")
public class CinemaAdminController {
    CinemaService cinemaService;
    @PostMapping
    public ApiResponse<CinemaResponse> createCinema(@Valid @RequestBody CinemaRequest cinemaRequest) {
        return ApiResponse.<CinemaResponse>builder()
                .code(200)
                .message("Create cinema successfully")
                .result(cinemaService.createCinema(cinemaRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CinemaResponse> updateCinema(@PathVariable Long id,
                                                    @Valid @RequestBody CinemaRequest cinemaRequest) {
        return ApiResponse.<CinemaResponse>builder()
                .code(200)
                .message("Update cinema successfully")
                .result(cinemaService.updateCinema(id, cinemaRequest))
                .build();
    }

    @GetMapping
    public ApiResponse<List<CinemaSummaryResponse>> getCinemas(@RequestParam String address) {
        return ApiResponse.<List<CinemaSummaryResponse>>builder()
                .code(200)
                .message("Get cinemas successfully")
                .result(cinemaService.getCinemas(address))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CinemaResponse> getCinema(@PathVariable Long id) {
        return ApiResponse.<CinemaResponse>builder()
                .code(200)
                .message("Get cinema successfully")
                .result(cinemaService.getCinema(id))
                .build();
    }

    @GetMapping("/cinemas")
    public ApiResponse<Page<CinemaResponse>> getListCinemas(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                            @RequestParam(required = false,defaultValue = "10") Integer sizeNumber) {
        return ApiResponse.<Page<CinemaResponse>>builder()
                .code(200)
                .message("Get cinemas successfully")
                .result(cinemaService.getListCinemas(pageNumber, sizeNumber))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCinema(@PathVariable Long id) {
        cinemaService.deleteCinema(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete cinema successfully")
                .build();
    }
    @GetMapping("/types")
    public ApiResponse<List<CinemaTypeResponse>> getCinemaTypes() {
        return ApiResponse.<List<CinemaTypeResponse>>builder()
                .code(200)
                .message("Get cinema types successfully")
                .result(cinemaService.getCinemaTypes())
                .build();
    }

}
