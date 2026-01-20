package com.example.english.Controller.Admin;

import com.example.english.Dto.Request.TicketPriceRequest;
import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.TicketPriceResponse;
import com.example.english.Entity.TicketPrice;
import com.example.english.Service.Interface.TicketPriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/admin/ticket-price")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Ticket Price Admin Controller", description = "APIs quản lý giá vé cho admin")
public class TicketPriceAdminController {
    TicketPriceService ticketPriceService;

    @PostMapping
    @Operation(summary = "Create Ticket Price", description = "API tạo mới giá vé")
    public ApiResponse<TicketPriceResponse> createTicketPrice(@RequestBody TicketPriceRequest ticketPriceRequest) {
        return ApiResponse.<TicketPriceResponse>builder()
                .code(200)
                .message("Create ticket price successfully")
                .result(ticketPriceService.createTicketPrice(ticketPriceRequest))
                .build();
    }

    @GetMapping("/seat-price")
    @Operation(summary = "Get Seat Ticket Price", description = "API lấy giá vé theo loại ghế, loại phòng chiếu, loại rạp, ngày và giờ")
    public ApiResponse<TicketPrice> getSeatTicketPrice(@RequestParam Long cinemaTypeId,
                                                      @RequestParam Long screenRoomTypeId,
                                                      @RequestParam Long seatTypeId,
                                                      @RequestParam LocalDate date,
                                                      @RequestParam LocalTime time) {
        return ApiResponse.<TicketPrice>builder()
                .code(200)
                .message("Get seat ticket price successfully")
                .result(ticketPriceService.getSeatTicketPrice(cinemaTypeId, screenRoomTypeId, seatTypeId, date, time))
                .build();
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update Ticket Price", description = "API cập nhật giá vé")
    public ApiResponse<TicketPriceResponse> updateTicketPrice(@PathVariable Long id, @RequestBody TicketPriceRequest ticketPriceRequest) {
        return ApiResponse.<TicketPriceResponse>builder()
                .code(200)
                .message("Update ticket price successfully")
                .result(ticketPriceService.updateTicketPrice(id, ticketPriceRequest))
                .build();
    }

    @GetMapping("/ticket-prices")
    @Operation(summary = "Get All Ticket Prices", description = "API lấy tất cả giá vé")
    public ApiResponse<List<TicketPriceResponse>> getAllTicketPrices() {
        return ApiResponse.<List<TicketPriceResponse>>builder()
                .code(200)
                .message("Get all ticket prices successfully")
                .result(ticketPriceService.getAllTicketPrice())
                .build();
    }
}
