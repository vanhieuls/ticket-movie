package com.example.english.Controller;

import com.example.english.Dto.Request.InvoiceRequest;
import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.InvoiceDetailResponse;
import com.example.english.Dto.Response.InvoiceResponse;
import com.example.english.Service.Interface.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/invoice")
@Slf4j
@Tag(name = "Invoice Controller", description = "APIs for invoice functionalities")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceController {
    InvoiceService invoiceService;

    @PostMapping
    @Operation(summary = "Create Invoice", description = "API tạo hóa đơn mới")
    public ApiResponse<Void> createInvoice(@RequestBody InvoiceRequest invoiceRequest) {
        invoiceService.createInvoice(invoiceRequest);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Create invoice successfully")
                .build();
    }

    @GetMapping("/list")
    @Operation(summary = "Get Invoice List", description = "API lấy danh sách hóa đơn của người dùng")
    public ApiResponse<List<InvoiceResponse>> getInvoiceList() {
        return ApiResponse.<List<InvoiceResponse>>builder()
                .code(200)
                .message("Get invoice list successfully")
                .result(invoiceService.getInvoiceList())
                .build();
    }

    @GetMapping("/{invoiceId}")
    @Operation(summary = "Get Invoice Detail", description = "API lấy chi tiết hóa đơn")
    public ApiResponse<InvoiceDetailResponse> getInvoiceDetail(@PathVariable Long invoiceId) {
        return ApiResponse.<InvoiceDetailResponse>builder()
                .code(200)
                .message("Get invoice detail successfully")
                .result(invoiceService.getInvoice(invoiceId))
                .build();
    }
}
