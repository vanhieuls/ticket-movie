package com.example.english.Controller.Admin;

import com.example.english.Dto.Request.SeatRequest;
import com.example.english.Dto.Response.*;
import com.example.english.Service.Interface.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RestController
@RequestMapping("/admin/seat")
@Tag(name = "Seat Admin Controller", description = "APIs quản lý ghế cho admin")
public class SeatAdminController {
    SeatService seatService;

    @PostMapping
    @Operation(summary = "Create Seats", description = "API tạo ghế cho phòng chiếu")
    public ApiResponse<List<SeatResponse>> createSeats(@RequestBody SeatRequest seatRequest) {
        return ApiResponse.<List<SeatResponse>>builder()
                .code(200)
                .message("Create seats successfully")
                .result(seatService.createSeats(seatRequest))
                .build();
    }

    @GetMapping("/screen-room/{screenRoomId}")
    @Operation(summary = "Get Seats by Screen Room", description = "API lấy danh sách ghế theo phòng chiếu")
    public ApiResponse<List<SeatSummaryRepo>> getSeatListByScreenRoom(@PathVariable Long screenRoomId) {
        return ApiResponse.<List<SeatSummaryRepo>>builder()
                .code(200)
                .message("Get seats by screen room successfully")
                .result(seatService.getSeatListByScreenRoom(screenRoomId))
                .build();
    }

    @PutMapping("/active/{id}")
    @Operation(summary = "Update Active Seat", description = "API cập nhật trạng thái hoạt động của ghế")
    public ApiResponse<Void> updateActiveSeat(@PathVariable Long id) {
        seatService.updateActiveSeat(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Update active seat successfully")
                .build();
    }
}
