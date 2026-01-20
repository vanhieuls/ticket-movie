package com.example.english.Controller;

import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Entity.Invoice;
import com.example.english.Service.Interface.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
@Slf4j
@Tag(name = "Ticket Controller", description = "APIs quản lý vé")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketController {
    TicketService ticketService;

    @PostMapping
    @Operation(summary = "Create Ticket", description = "API tạo vé mới" )
    public ApiResponse<Void> createTicket(@RequestParam String vnp_TxRef, @RequestBody Invoice invoice) {
        ticketService.createTicket(vnp_TxRef, invoice);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Create ticket successfully")
                .build();
    }
}
