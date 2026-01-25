package com.example.english.Controller;

import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.SeatShowTime;
import com.example.english.Service.Interface.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seat")
@Slf4j
@Tag(name = "Seat Controller", description = "APIs for seat functionalities")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeatController {
    SeatService seatService;

    @GetMapping("/showtime/{showTimeId}/seats")
    @Operation(summary = "Get Seats by ShowTime", description = "API lấy danh sách ghế theo suất chiếu")
    public ApiResponse<SeatShowTime> getListSeat(@PathVariable Long showTimeId) {
        return ApiResponse.<SeatShowTime>builder()
                .code(200)
                .message("Get seats by showtime successfully")
                .result(seatService.getListSeat(showTimeId))
                .build();
    }
}
