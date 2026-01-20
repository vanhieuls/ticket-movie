package com.example.english.Controller.Admin;

import com.example.english.Dto.Request.CinemaRequest;
import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.CinemaResponse;
import com.example.english.Dto.Response.CinemaSummaryResponse;
import com.example.english.Dto.Response.CinemaTypeResponse;
import com.example.english.Service.Interface.CinemaService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Create Cinema", description = "API tạo mới rạp chiếu phim")
    @PostMapping
    public ApiResponse<CinemaResponse> createCinema(@Valid @RequestBody CinemaRequest cinemaRequest) {
        return ApiResponse.<CinemaResponse>builder()
                .code(200)
                .message("Create cinema successfully")
                .result(cinemaService.createCinema(cinemaRequest))
                .build();
    }
    @Operation(summary = "Update Cinema", description = "API cập nhật thông tin rạp chiếu phim")
    @PutMapping("/{id}")
    public ApiResponse<CinemaResponse> updateCinema(@PathVariable Long id,
                                                    @Valid @RequestBody CinemaRequest cinemaRequest) {
        return ApiResponse.<CinemaResponse>builder()
                .code(200)
                .message("Update cinema successfully")
                .result(cinemaService.updateCinema(id, cinemaRequest))
                .build();
    }
    @Operation(summary = "Get Cinemas", description = "API lấy danh sách rạp chiếu phim theo địa chỉ")
    @GetMapping
    public ApiResponse<List<CinemaSummaryResponse>> getCinemas(@RequestParam String address) {
        return ApiResponse.<List<CinemaSummaryResponse>>builder()
                .code(200)
                .message("Get cinemas successfully")
                .result(cinemaService.getCinemas(address))
                .build();
    }
    @Operation(summary = "Get Cinema", description = "API lấy thông tin chi tiết rạp chiếu phim")
    @GetMapping("/{id}")
    public ApiResponse<CinemaResponse> getCinema(@PathVariable Long id) {
        return ApiResponse.<CinemaResponse>builder()
                .code(200)
                .message("Get cinema successfully")
                .result(cinemaService.getCinema(id))
                .build();
    }
    @Operation(summary = "Get List of Cinemas", description = "API lấy danh sách rạp chiếu phim có phân trang")
    @GetMapping("/cinemas")
    public ApiResponse<Page<CinemaResponse>> getListCinemas(@RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                            @RequestParam(required = false,defaultValue = "10") Integer sizeNumber) {
        return ApiResponse.<Page<CinemaResponse>>builder()
                .code(200)
                .message("Get cinemas successfully")
                .result(cinemaService.getListCinemas(pageNumber, sizeNumber))
                .build();
    }
    @Operation(summary = "Delete Cinema", description = "API xóa rạp chiếu phim")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCinema(@PathVariable Long id) {
        cinemaService.deleteCinema(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete cinema successfully")
                .build();
    }
    @Operation(summary = "Get Cinema Types", description = "API lấy danh sách loại rạp chiếu phim")
    @GetMapping("/types")
    public ApiResponse<List<CinemaTypeResponse>> getCinemaTypes() {
        return ApiResponse.<List<CinemaTypeResponse>>builder()
                .code(200)
                .message("Get cinema types successfully")
                .result(cinemaService.getCinemaTypes())
                .build();
    }

}
