package com.example.english.Controller.Staff;

import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.InvoiceDetailResponse;
import com.example.english.Entity.Invoice;
import com.example.english.Enum.InvoiceStatus;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Service.Interface.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staff")
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Staff Controller", description = "APIs for staff functionalities")
public class StaffController {
    InvoiceService invoiceService;

    @GetMapping("/invoice")
    @Operation(
            summary = "Get Invoice by Booking Code",
            description = "API to retrieve invoice details using the booking code"
    )
    public ApiResponse<InvoiceDetailResponse> getInvoiceByBookingCode(@RequestParam String bookingCode) {
        return ApiResponse.<InvoiceDetailResponse>builder()
                .code(200)
                .message("Invoice retrieved successfully")
                .result(invoiceService.getInvoiceByBookingCode(bookingCode))
                .build();
    }

    @PostMapping("/print/{invoiceId}")
    @Operation(
            summary = "Check-In Invoice",
            description = "API to check-in an invoice by its ID"
    )
    public ApiResponse<Void> checkIn(@PathVariable Long invoiceId) {
        invoiceService.checkInInvoice(invoiceId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Check-in successful")
                .build();
    }
}

