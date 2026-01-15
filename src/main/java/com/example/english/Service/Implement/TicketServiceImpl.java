package com.example.english.Service.Implement;

import com.example.english.Dto.TxnInfo;
import com.example.english.Entity.*;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Repository.SeatRepository;
import com.example.english.Repository.ShowTimeRepository;
import com.example.english.Repository.TicketRepository;
import com.example.english.Service.Interface.TicketPriceService;
import com.example.english.Service.Interface.TicketService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketServiceImpl implements TicketService {
    RedisService redisService;
    ShowTimeRepository showTimeRepository;
    SeatRepository seatRepository;
    TicketPriceService ticketPriceService;
    TicketRepository ticketRepository;
    @Override
    public void createTicket(String vnp_TxRef, Invoice invoice) {
        TxnInfo txnInfo = redisService.getByTxnRef(vnp_TxRef);
        ShowTime showTime = showTimeRepository.findById(txnInfo.getShowTimeId()).orElseThrow(
                ()-> new AppException(ErrorCode.SHOW_TIME_NOT_FOUND)
        );
        Long cinemaType = showTime.getScreenRoom().getCinema().getCinemaType().getId();
        Long screenRoomType = showTime.getScreenRoom().getScreenRoomType().getId();
        LocalDate date = showTime.getShowDate();
        LocalTime time = showTime.getStartTime();
        Map<Long, TicketPrice> ticketPriceMap = new HashMap<>();
        List<Seat> seatList = seatRepository.findAllById(txnInfo.getListSeatId());
        for(Seat seat : seatList){
            Long seatTypeId = seat.getSeatType().getId();
            if(!ticketPriceMap.containsKey(seatTypeId)){
                TicketPrice ticketPrice = ticketPriceService.getSeatTicketPrice(
                        cinemaType,
                        screenRoomType,
                        seatTypeId,
                        date,
                        time
                );
                ticketPriceMap.put(seatTypeId, ticketPrice);
            }
            Ticket ticket = Ticket.builder()
                    .seat(seat)
                    .showTime(showTime)
                    .invoice(invoice)
                    .ticketPrice(ticketPriceMap.get(seatTypeId))
                    .build();
            ticketRepository.save(ticket);
        }
    }
}
