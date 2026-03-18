package com.example.english.Service.Interface;

import com.example.english.Dto.Request.TicketPriceRequest;
import com.example.english.Dto.Response.TicketPriceResponse;
import com.example.english.Entity.TicketPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TicketPriceService {
    TicketPriceResponse createTicketPrice(TicketPriceRequest ticketPriceRequest);

    TicketPrice getSeatTicketPrice(Long cinemaTypeId,
                                   Long screenRoomTypeId,
                                   Long seatTypeId,
                                   LocalDate date,
                                   LocalTime time);

    TicketPriceResponse updateTicketPrice(Long id, TicketPriceRequest ticketPriceRequest);

    List<TicketPriceResponse> getAllTicketPrice();

    Page<TicketPriceResponse> getAllTicketPrices(Pageable pageable);
}
