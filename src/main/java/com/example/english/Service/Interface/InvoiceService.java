package com.example.english.Service.Interface;

import com.example.english.Dto.Request.InvoiceRequest;
import com.example.english.Dto.Response.InvoiceDetailAD;
import com.example.english.Dto.Response.InvoiceDetailResponse;
import com.example.english.Dto.Response.InvoiceResponse;
import com.example.english.Dto.Response.InvoiceSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InvoiceService {
    void createInvoice(InvoiceRequest invoiceRequest);
    void updateInvoice(String vnp_TxnRef);
    List<InvoiceResponse> getInvoiceList();
    Page<InvoiceResponse> getInvoiceList(Pageable pageable);
    InvoiceDetailResponse getInvoice(Long invoiceId);
    InvoiceDetailResponse getInvoiceByBookingCode(String bookingCode);
    void checkInInvoice(Long invoiceId);
    List<InvoiceSummary> getInvoiceSummaryList();
    Page<InvoiceSummary> getInvoiceSummaryList(Pageable pageable);
    InvoiceDetailAD getInvoiceDetail(Long id);
}
