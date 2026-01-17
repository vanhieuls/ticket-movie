package com.example.english.Mapper;

import com.example.english.Dto.Response.InvoiceDetailAD;
import com.example.english.Dto.Response.InvoiceDetailResponse;
import com.example.english.Dto.Response.InvoiceResponse;
import com.example.english.Dto.Response.InvoiceSummary;
import com.example.english.Entity.Invoice;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    InvoiceDetailResponse toInvoiceDetailResponse(Invoice invoice);
    InvoiceResponse toInvoiceResponse(Invoice invoice);
    List<InvoiceResponse> toInvoiceResponseList(List<Invoice> invoices);
    InvoiceSummary toInvoiceSummary(Invoice invoice);
    List<InvoiceSummary> toInvoiceSummaryList(List<Invoice> invoices);
    InvoiceDetailAD toInvoiceDetailAD(Invoice invoice);
}
