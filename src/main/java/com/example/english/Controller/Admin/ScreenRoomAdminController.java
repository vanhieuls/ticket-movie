package com.example.english.Controller.Admin;

import com.example.english.Dto.Request.ScreenRoomRequest;
import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.ScreenRoomDetailResponse;
import com.example.english.Dto.Response.ScreenRoomResponse;
import com.example.english.Entity.ScreenRoom;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Service.Interface.ScreenRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/screenRoom")
@Slf4j
@Tag(name = "Screen Room Admin Controller", description = "APIs quản lý phòng chiếu cho admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ScreenRoomAdminController {
    ScreenRoomService screenRoomService;

    @PostMapping
    @Operation(summary = "Create Screen Room", description = "API tạo mới phòng chiếu")
    public ApiResponse<ScreenRoomResponse> createScreenRoom(@RequestBody ScreenRoomRequest screenRoomRequest) {
        return ApiResponse.<ScreenRoomResponse>builder()
                .code(200)
                .message("Create screen room successfully")
                .result(screenRoomService.createScreenRoom(screenRoomRequest))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Screen Room", description = "API cập nhật phòng chiếu")
    public ApiResponse<ScreenRoomResponse> updateScreenRoom(@PathVariable Long id,
                                                                    @RequestBody ScreenRoomRequest screenRoomRequest) {
        return ApiResponse.<ScreenRoomResponse>builder()
                .code(200)
                .message("Update screen room successfully")
                .result(screenRoomService.updateScreenRoom(id, screenRoomRequest))
                .build();
    }

    @GetMapping("/{cinemaId}")
    @Operation(summary = "Get Screen Room List", description = "API lấy danh sách phòng chiếu theo rạp")
    public ApiResponse<List<ScreenRoomDetailResponse>> getScreenRoomList(@PathVariable Long cinemaId) {
        return ApiResponse.<List<ScreenRoomDetailResponse>>builder()
                .code(200)
                .message("Get screen room list successfully")
                .result(screenRoomService.getScreenRoomList(cinemaId))
                .build();
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "Get Screen Room Detail", description = "API lấy chi tiết phòng chiếu")
    public ApiResponse<ScreenRoomResponse> getScreenRoom(@PathVariable Long id) {
        return ApiResponse.<ScreenRoomResponse>builder()
                .code(200)
                .message("Get screen room detail successfully")
                .result(screenRoomService.getScreenRoomDetail(id))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Screen Room", description = "API xóa phòng chiếu")
    public ApiResponse<Void> deleteScreenRoom(@PathVariable Long id) {
        screenRoomService.deleteScreenRoom(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete screen room successfully")
                .build();
    }

    @GetMapping("/active/cinema/{cinemaId}")
    @Operation(summary = "Get Screen Room Active List", description = "API lấy danh sách phòng chiếu đang hoạt động theo rạp")
    public ApiResponse<List<ScreenRoomDetailResponse>> getScreenRoomActiveList(@PathVariable Long cinemaId) {
        return ApiResponse.<List<ScreenRoomDetailResponse>>builder()
                .code(200)
                .message("Get screen room active list successfully")
                .result(screenRoomService.getScreenRoomActiveList(cinemaId))
                .build();
    }
}
