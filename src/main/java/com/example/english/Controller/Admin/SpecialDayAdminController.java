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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/special-day")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Special Day Admin Controller", description = "APIs quản lý ngày đặc biệt cho admin")
@PreAuthorize("hasRole('ADMIN')")
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

    @GetMapping("/special-days/page")
    @Operation(summary = "Get All Special Days with Pagination", description = "API lấy tất cả ngày đặc biệt có phân trang")
    public ApiResponse<Page<SpecialDay>> getAllSpecialDays(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ApiResponse.<Page<SpecialDay>>builder()
                .code(200)
                .message("Get all special days with pagination successfully")
                .result(specialDayService.getAllSpecialDays(pageable))
                .build();
    }
}
