package com.example.english.Controller.Admin;

import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.InvoiceDetailAD;
import com.example.english.Dto.Response.InvoiceSummary;
import com.example.english.Entity.Invoice;
import com.example.english.Entity.Ticket;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Service.Interface.InvoiceService;
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

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/invoices")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Invoice Admin Controller", description = "APIs quản lý hóa đơn cho admin")
public class InvoiceAdminController {
    InvoiceService invoiceService;

    @Operation(summary = "Get Invoice Summary List", description = "API lấy danh sách tóm tắt hóa đơn")
    @GetMapping("/invoices")
    public ApiResponse<List<InvoiceSummary>> getInvoiceSummaryList() {
        return ApiResponse.<List<InvoiceSummary>>builder()
                .code(200)
                .message("Get invoice summary list successfully")
                .result(invoiceService.getInvoiceSummaryList())
                .build();
    }

    @GetMapping("/invoices/{id}")
    @Operation(summary = "Get Invoice Detail", description = "API lấy chi tiết hóa đơn)")
    public ApiResponse<InvoiceDetailAD> getInvoiceDetail(@PathVariable Long id) {
        return ApiResponse.<InvoiceDetailAD>builder()
                .code(200)
                .message("Get invoice detail successfully")
                .result(invoiceService.getInvoiceDetail(id))
                .build();
    }
}
