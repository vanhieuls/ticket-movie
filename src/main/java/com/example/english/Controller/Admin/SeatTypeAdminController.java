package com.example.english.Controller.Admin;

import com.example.english.Dto.Request.SeatTypeRequest;
import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.SeatTypeResponse;
import com.example.english.Entity.SeatType;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Service.Interface.SeatTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/seat-type")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Seat Type Admin Controller", description = "APIs quản lý loại ghế cho admin")
public class SeatTypeAdminController {
    SeatTypeService seatTypeService;

    @PostMapping
    @Operation(summary = "Create Seat Type", description = "API tạo mới loại ghế")
    public ApiResponse<SeatTypeResponse> createSeatType(@RequestBody SeatTypeRequest seatTypeRequest) {
        return ApiResponse.<SeatTypeResponse>builder()
                .code(200)
                .message("Create seat type successfully")
                .result(seatTypeService.createSeatType(seatTypeRequest))
                .build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Seat Type", description = "API cập nhật loại ghế")
    public ApiResponse<SeatTypeResponse> updateSeatType(@PathVariable Long id, @RequestBody SeatTypeRequest seatTypeRequest) {
        return ApiResponse.<SeatTypeResponse>builder()
                .code(200)
                .message("Update seat type successfully")
                .result(seatTypeService.updateSeatType(id, seatTypeRequest))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Seat Type", description = "API lấy thông tin loại ghế theo ID")
    public ApiResponse<SeatTypeResponse> getSeatType(@PathVariable Long id) {
        return ApiResponse.<SeatTypeResponse>builder()
                .code(200)
                .message("Get seat type successfully")
                .result(seatTypeService.getSeatType(id))
                .build();
    }

   @PutMapping("/status/{id}")
   @Operation(summary = "Update Status Seat Type", description = "API cập nhật trạng thái loại ghế")
   public ApiResponse<Void> updateStatusSeatType(@PathVariable Long id) {
       seatTypeService.updateStatusSeatType(id);
       return ApiResponse.<Void>builder()
               .code(200)
               .message("Update status seat type successfully")
               .build();
   }

    @GetMapping("/seat-types")
    @Operation(summary = "Get All Seat Types", description = "API lấy tất cả loại ghế")
    public ApiResponse<List<SeatTypeResponse>> getAllSeatType() {
        return ApiResponse.<List<SeatTypeResponse>>builder()
                .code(200)
                .message("Get all seat types successfully")
                .result(seatTypeService.getAllSeatType())
                .build();
    }
}
