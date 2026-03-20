package com.example.english.Controller.Admin;

import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.InvoiceDetailAD;
import com.example.english.Dto.Response.InvoiceSummary;
import com.example.english.Service.Interface.InvoiceService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/admin/invoices")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Invoice Admin Controller", description = "APIs quản lý hóa đơn cho admin")
@PreAuthorize("hasRole('ADMIN')")
public class InvoiceAdminController {
    InvoiceService invoiceService;

    @Operation(summary = "Get Invoice Summary List", description = "API lấy danh sách tóm tắt hóa đơn")
    @GetMapping
    public ApiResponse<List<InvoiceSummary>> getInvoiceSummaryList() {
        return ApiResponse.<List<InvoiceSummary>>builder()
                .code(200)
                .message("Get invoice summary list successfully")
                .result(invoiceService.getInvoiceSummaryList())
                .build();
    }

    @Operation(summary = "Get Invoice Summary List with Pagination", description = "API lấy danh sách tóm tắt hóa đơn có phân trang")
    @GetMapping("/page")
    public ApiResponse<Page<InvoiceSummary>> getInvoiceSummaryList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ApiResponse.<Page<InvoiceSummary>>builder()
                .code(200)
                .message("Get invoice summary list with pagination successfully")
                .result(invoiceService.getInvoiceSummaryList(pageable))
                .build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Invoice Detail", description = "API lấy chi tiết hóa đơn)")
    public ApiResponse<InvoiceDetailAD> getInvoiceDetail(@PathVariable Long id) {
        return ApiResponse.<InvoiceDetailAD>builder()
                .code(200)
                .message("Get invoice detail successfully")
                .result(invoiceService.getInvoiceDetail(id))
                .build();
    }
}
