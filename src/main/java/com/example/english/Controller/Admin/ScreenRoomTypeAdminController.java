package com.example.english.Controller.Admin;

import com.example.english.Dto.Request.ScreenRoomTypeRequest;
import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.ScreenRoomTypeResponse;
import com.example.english.Entity.ScreenRoomType;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Service.Interface.ScreenRoomTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/screenRoomType")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Screen Room Type Admin Controller", description = "APIs quản lý loại phòng chiếu cho admin")
public class ScreenRoomTypeAdminController {
    ScreenRoomTypeService  screenRoomTypeService;

    @PostMapping
    @Operation(summary = "Create Screen Room Type", description = "API tạo mới loại phòng chiếu")
    public ApiResponse<ScreenRoomTypeResponse> createScreenRoomType(@RequestBody ScreenRoomTypeRequest screenRoomTypeRequest) {
        return ApiResponse.<ScreenRoomTypeResponse>builder()
                .code(200)
                .message("Create screen room type successfully")
                .result(screenRoomTypeService.createScreenRoomType(screenRoomTypeRequest))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Screen Room Type", description = "API cập nhật loại phòng chiếu")
    public ApiResponse<ScreenRoomTypeResponse> updateScreenRoomType(@PathVariable Long id, @RequestBody ScreenRoomTypeRequest screenRoomTypeRequest) {
        return ApiResponse.<ScreenRoomTypeResponse>builder()
                .code(200)
                .message("Update screen room type successfully")
                .result(screenRoomTypeService.updateScreenRoomType(id, screenRoomTypeRequest))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Screen Room Type", description = "API lấy thông tin loại phòng chiếu theo ID")
    public ApiResponse<ScreenRoomTypeResponse> getScreenRoomType(@PathVariable Long id) {
        return ApiResponse.<ScreenRoomTypeResponse>builder()
                .code(200)
                .message("Get screen room type successfully")
                .result(screenRoomTypeService.getScreenRoomType(id))
                .build();
    }

    @GetMapping("/screen-room-types")
    public ApiResponse<List<ScreenRoomTypeResponse>> getAllScreenRoomType() {
        return ApiResponse.<List<ScreenRoomTypeResponse>>builder()
                .code(200)
                .message("Get all screen room types successfully")
                .result(screenRoomTypeService.getAllScreenRoomType())
                .build();
    }

    @PutMapping("/status/{id}")
    @Operation(summary = "Update Status Screen Room Type", description = "API cập nhật trạng thái loại phòng chiếu")
    public ApiResponse<Void> updateStatusScreenRoomType(@PathVariable Long id) {
        screenRoomTypeService.updateStatusScreenRoomType(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Update status screen room type successfully")
                .build();
    }
}
