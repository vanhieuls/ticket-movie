package com.example.english.Service.Interface;

import com.example.english.Entity.Invoice;

public interface TicketService {
    void createTicket(String vnp_TxRef, Invoice invoice);
}
