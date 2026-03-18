package com.example.english.Controller.Admin;

import com.example.english.Dto.Request.ScreenRoomTypeRequest;
import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.ScreenRoomTypeResponse;
import com.example.english.Service.Interface.ScreenRoomTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ApiResponse<Page<ScreenRoomTypeResponse>> getAllScreenRoomType(@RequestParam(required = false) Boolean status,
                                                                          @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
                                                                          @RequestParam(required = false, defaultValue = "10") Integer sizeNumber,
                                                                          @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                                          @RequestParam(required = false, defaultValue = "desc") String sortDir) {
        return ApiResponse.<Page<ScreenRoomTypeResponse>>builder()
                .code(200)
                .message("Get all screen room types successfully")
                .result(screenRoomTypeService.getAllScreenRoomType(pageNumber, sizeNumber, sortBy, sortDir, status))
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
