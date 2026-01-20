package com.example.english.Controller.Admin;

import com.example.english.Dto.Request.ShowTimeRequest;
import com.example.english.Dto.Response.*;
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
@RequestMapping("/admin/showtime")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "ShowTime Admin Controller", description = "APIs quản lý suất chiếu cho admin")
public class ShowTimeAdminController {
    ShowTimeService showTimeService;

    @PostMapping
    @Operation(summary = "Create Show Time", description = "API tạo mới suất chiếu")
    public ApiResponse<ShowTimeResponse> createShowTime(@RequestBody ShowTimeRequest showTimeRequest) {
        return ApiResponse.<ShowTimeResponse>builder()
                .code(200)
                .message("Create show time successfully")
                .result(showTimeService.createShowTime(showTimeRequest))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Show Time", description = "API cập nhật suất chiếu")
    public ApiResponse<ShowTimeResponse> updateShowTime(@PathVariable Long id, @RequestBody ShowTimeRequest showTimeRequest) {
        return ApiResponse.<ShowTimeResponse>builder()
                .code(200)
                .message("Update show time successfully")
                .result(showTimeService.updateShowTime(showTimeRequest, id))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Show Time", description = "API lấy thông tin suất chiếu theo ID")
    public ApiResponse<ShowTimeResponse> getShowTime(@PathVariable Long id) {
        return ApiResponse.<ShowTimeResponse>builder()
                .code(200)
                .message("Get show time successfully")
                .result(showTimeService.getShowTime(id))
                .build();
    }

    @GetMapping("/screen-room/{screenRoomId}")
    @Operation(summary = "Get Show Time List by Screen Room", description = "API lấy danh sách suất chiếu theo phòng chiếu và ngày")
    public ApiResponse<List<ShowTimeDto>> getShowTimeList(@PathVariable Long screenRoomId,
                                                           @RequestParam("date") LocalDate date) {
        return ApiResponse.<List<ShowTimeDto>>builder()
                .code(200)
                .message("Get show time list successfully")
                .result(showTimeService.getShowTimeList(screenRoomId, date))
                .build();
    }

    @GetMapping("/cinema/{cinemaId}")
    @Operation(summary = "Get Show Time List by Cinema", description = "API lấy danh sách suất chiếu theo rạp và ngày")
    public ApiResponse<List<MovieShowTimeResponse>> getShowTimeListByCinema(@PathVariable Long cinemaId,
                                                                 @RequestParam("date") LocalDate date) {
        return ApiResponse.<List<MovieShowTimeResponse>>builder()
                .code(200)
                .message("Get show time list by cinema successfully")
                .result(showTimeService.getListShowTimeFilterCinema(cinemaId, date))
                .build();
    }

    @GetMapping("/movie/{movieId}")
    @Operation(summary = "Get Show Time List by Movie", description = "API lấy danh sách suất chiếu theo phim, địa chỉ rạp và ngày")
    public ApiResponse<List<CinemaShowTimeResponse>> getShowTimeListByMovie(@PathVariable Long movieId,
                                                                             @RequestParam("date") LocalDate date,
                                                                             @RequestParam("address") String address) {
        return ApiResponse.<List<CinemaShowTimeResponse>>builder()
                .code(200)
                .message("Get show time list by movie successfully")
                .result(showTimeService.getListShowTimeFilterMovie(movieId, date, address))
                .build();
    }

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

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Show Time", description = "API xóa suất chiếu")
    public ApiResponse<Void> deleteShowTime(@PathVariable Long id) {
        showTimeService.deleteShowTime(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete show time successfully")
                .build();
    }
}

