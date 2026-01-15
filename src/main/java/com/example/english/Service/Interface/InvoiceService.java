package com.example.english.Service.Interface;

import com.example.english.Dto.Request.InvoiceRequest;
import com.example.english.Dto.Response.InvoiceDetailAD;
import com.example.english.Dto.Response.InvoiceDetailResponse;
import com.example.english.Dto.Response.InvoiceResponse;
import com.example.english.Dto.Response.InvoiceSummary;

import java.util.List;

public interface InvoiceService {
    void createInvoice(InvoiceRequest invoiceRequest);
    void updateInvoice(String vnp_TxnRef);
    List<InvoiceResponse> getInvoiceList();
    InvoiceDetailResponse getInvoice(Long invoiceId);
    InvoiceDetailResponse getInvoiceByBookingCode(String bookingCode);
    void checkInInvoice(Long invoiceId);
    List<InvoiceSummary> getInvoiceSummaryList();
    InvoiceDetailAD getInvoiceDetail(Long id);
}
