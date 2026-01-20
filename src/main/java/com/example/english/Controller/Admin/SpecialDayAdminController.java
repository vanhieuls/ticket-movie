package com.example.english.Controller.Admin;

import com.example.english.Dto.Request.SpecialDayRequest;
import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Entity.SpecialDay;
import com.example.english.Service.Interface.SpecialDayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/special-day")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Special Day Admin Controller", description = "APIs quản lý ngày đặc biệt cho admin")
public class SpecialDayAdminController {
    SpecialDayService specialDayService;

    @PostMapping
    @Operation(summary = "Create Special Day", description = "API tạo mới ngày đặc biệt")
    public ApiResponse<SpecialDay> createSpecialDay(@RequestBody SpecialDayRequest specialDayRequest) {
       return ApiResponse.<SpecialDay>builder()
                .code(200)
                .message("Create special day successfully")
                .result(specialDayService.createSpecialDay(specialDayRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Special Day", description = "API xóa ngày đặc biệt")
    public ApiResponse<Void> deleteSpecialDay(@PathVariable Long id) {
        specialDayService.deleteSpecialDay(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete special day successfully")
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Special Day", description = "API cập nhật ngày đặc biệt")
    public ApiResponse<SpecialDay> updateSpecialDay(@PathVariable Long id,
                                                    @RequestBody SpecialDayRequest specialDayRequest) {
        return ApiResponse.<SpecialDay>builder()
                .code(200)
                .message("Update special day successfully")
                .result(specialDayService.updateSpecialDay(id, specialDayRequest))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Special Day", description = "API lấy thông tin ngày đặc biệt theo ID")
    public ApiResponse<SpecialDay> getSpecialDayById(@PathVariable Long id) {
        return ApiResponse.<SpecialDay>builder()
                .code(200)
                .message("Get special day successfully")
                .result(specialDayService.getSpecialDayById(id))
                .build();
    }

    @GetMapping("/special-days")
    @Operation(summary = "Get All Special Days", description = "API lấy tất cả ngày đặc biệt")
    public ApiResponse<List<SpecialDay>> getAllSpecialDays() {
        return ApiResponse.<List<SpecialDay>>builder()
                .code(200)
                .message("Get all special days successfully")
                .result(specialDayService.getAllSpecialDays())
                .build();
    }
}
