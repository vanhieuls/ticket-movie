package com.example.english.Service.Implement;

import com.example.english.Configuration.RandomCode;
import com.example.english.Dto.Request.InvoiceRequest;
import com.example.english.Dto.Response.InvoiceDetailAD;
import com.example.english.Dto.Response.InvoiceDetailResponse;
import com.example.english.Dto.Response.InvoiceResponse;
import com.example.english.Dto.Response.InvoiceSummary;
import com.example.english.Dto.TxnInfo;
import com.example.english.Entity.*;
import com.example.english.Enum.InvoiceStatus;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Repository.*;
import com.example.english.Service.Interface.InvoiceService;
import com.example.english.Service.Interface.TicketPriceService;
import com.example.english.Service.Interface.TicketService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceServiceImpl implements InvoiceService {
    RedisService redisService;
    UserRepository userRepository;
    ShowTimeRepository showTimeRepository;
    SeatRepository seatRepository;
    TicketPriceService ticketPriceService;
    InvoiceRepository invoiceRepository;
    TicketService ticketService;
    TicketRepository ticketRepository;
    private User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        assert userDetails != null;
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private BigDecimal parseAmount(String vnpAmount) {
        long amount = Long.parseLong(vnpAmount);
        return BigDecimal.valueOf(amount);
    }
    Ticket getTicket(Long invoiceId) {
        return ticketRepository.findFirstByInvoice_Id(invoiceId)
                .orElseThrow(()->new AppException(ErrorCode.INVOICE_NOT_EXISTED));
    }
    @Transactional
    @Override
    public void createInvoice(InvoiceRequest invoiceRequest) {
        ShowTime showTime = showTimeRepository.findById(invoiceRequest.getShowtimeId())
                .orElseThrow(() -> new AppException(ErrorCode.SHOW_TIME_NOT_FOUND));
        Long cinemaType = showTime.getScreenRoom().getCinema().getCinemaType().getId();
        Long screenRoomType = showTime.getScreenRoom().getScreenRoomType().getId();
        LocalDate date = showTime.getShowDate();
        LocalTime time = showTime.getStartTime();

        Map<Long, TicketPrice> ticketPriceMap = new HashMap<>();

        List<Seat> seatEntities = seatRepository.findAllById(invoiceRequest.getListSeatId());

        TxnInfo txnInfo = new TxnInfo();
        txnInfo.setListSeatId(invoiceRequest.getListSeatId());
        txnInfo.setShowTimeId(invoiceRequest.getShowtimeId());
        redisService.txnRefInfo(invoiceRequest.getVnp_TxnRef(), txnInfo, 10L);
        BigDecimal totalMoney = new BigDecimal(0);

        for (Seat seat : seatEntities) {
            redisService.seatHolding(invoiceRequest.getShowtimeId(), seat.getId(), 10L);
            Long seatTypeId = seat.getSeatType().getId();
            if(!ticketPriceMap.containsKey(seatTypeId)) {
                TicketPrice ticketPrice = ticketPriceService.getSeatTicketPrice(
                        cinemaType,
                        screenRoomType,
                        seatTypeId,
                        date,
                        time
                );
                ticketPriceMap.put(seatTypeId, ticketPrice);
            }
            totalMoney = totalMoney.add(ticketPriceMap.get(seatTypeId).getPrice());
            continue;
        }

        if(!totalMoney.equals(parseAmount(invoiceRequest.getVnp_Amount()))){
            throw new AppException(ErrorCode.INVALID_PRICE);
        }

        Invoice invoiceEntity = new Invoice();

        invoiceEntity.setCreatedDate(LocalDate.now());
        invoiceEntity.setCreatedTime(LocalTime.now());
        invoiceEntity.setUser(getUser());
        invoiceEntity.setTotalAmount(totalMoney);
        invoiceEntity.setTxnRef(invoiceRequest.getVnp_TxnRef());
        invoiceEntity.setStatus(InvoiceStatus.PENDING.toString());

        try {
            invoiceRepository.save(invoiceEntity);
        } catch (DataAccessException dae) {
            throw new AppException(ErrorCode.ORDER_CREATE_FAILED);
        }
    }

    @Override
    public void updateInvoice(String vnp_TxnRef) {
        Invoice invoiceEntity = invoiceRepository.findByTxnRef(vnp_TxnRef)
                .orElseThrow(()->new AppException(ErrorCode.INVOICE_NOT_EXISTED));
        String bookingCode = RandomCode.generate(12);
        invoiceEntity.setStatus(String.valueOf(InvoiceStatus.PAID));
        invoiceEntity.setBookingCode(bookingCode);
        try {
            ticketService.createTicket(vnp_TxnRef, invoiceRepository.save(invoiceEntity));
        }catch (DataAccessException e){
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    @Override
    public List<InvoiceResponse> getInvoiceList() {
        User user = getUser();
        List<Invoice> invoiceEntityList = invoiceRepository.findByUser_Id(user.getId());
        List<InvoiceResponse> invoiceResponseList = new ArrayList<>();
        for (Invoice invoiceEntity : invoiceEntityList) {
            Ticket ticketEntity = getTicket(invoiceEntity.getId());
            int totalTicket = ticketRepository.countByInvoice_Id(invoiceEntity.getId());
            InvoiceResponse invoiceResponse = InvoiceResponse.builder()
                    .invoiceId(invoiceEntity.getId())
                    .totalMoney(invoiceEntity.getTotalAmount())
                    .movieName(ticketEntity.getShowTime().getMovie().getName())
                    .totalTicket(totalTicket)
                    .showDate(ticketEntity.getShowTime().getShowDate())
                    .startTime(ticketEntity.getShowTime().getStartTime())
                    .build();
            invoiceResponseList.add(invoiceResponse);
        }
        return invoiceResponseList;
    }


    @Override
    public InvoiceDetailResponse getInvoice(Long invoiceId) {
        Invoice invoiceEntity = invoiceRepository.findById(invoiceId)
                .orElseThrow(()->new AppException(ErrorCode.INVOICE_NOT_EXISTED));
        return  getInvoiceDetail(invoiceEntity);
    }

    @Override
    public InvoiceDetailResponse getInvoiceByBookingCode(String bookingCode) {
        Invoice invoiceEntity = invoiceRepository.findByBookingCode(bookingCode)
                .orElseThrow(()->new AppException(ErrorCode.INVOICE_NOT_EXISTED));
        return getInvoiceDetail(invoiceEntity);
    }

    @Override
    public void checkInInvoice(Long invoiceId) {
        Invoice invoiceEntity = invoiceRepository.findById(invoiceId)
                .orElseThrow(()->new AppException(ErrorCode.INVOICE_NOT_EXISTED));
        invoiceEntity.setStatus(String.valueOf(InvoiceStatus.CHECKED_IN));
        invoiceRepository.save(invoiceEntity);
    }

    @Override
    public List<InvoiceSummary> getInvoiceSummaryList() {
        List<Invoice> invoiceEntityList = invoiceRepository.findAll();
        List<InvoiceSummary> invoiceSummaryList = new ArrayList<>();
        for (Invoice invoiceEntity : invoiceEntityList) {
            Ticket ticketEntity = getTicket(invoiceEntity.getId());
            String showtime = ticketEntity.getShowTime().getStartTime().toString() + "-" +
                    ticketEntity.getShowTime().getEndTime().toString() + " " +
                    ticketEntity.getShowTime().getShowDate().toString();
            String screenRoom = ticketEntity.getShowTime().getScreenRoom().getName() + " - " +
                    ticketEntity.getShowTime().getScreenRoom().getCinema().getName();
            InvoiceSummary invoiceSummary = InvoiceSummary.builder()
                    .id(invoiceEntity.getId())
                    .code(invoiceEntity.getBookingCode())
                    .movieName(ticketEntity.getShowTime().getMovie().getName())
                    .showTime(showtime)
                    .screenRoom(screenRoom)
                    .totalMoney(invoiceEntity.getTotalAmount())
                    .createDate(invoiceEntity.getCreatedDate().toString())
                    .build();
            invoiceSummaryList.add(invoiceSummary);
        }
        return invoiceSummaryList;
    }

    @Override
    public InvoiceDetailAD getInvoiceDetail(Long id) {
        Invoice invoiceEntity = invoiceRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.INVOICE_NOT_EXISTED));
        Ticket ticketEntity = getTicket(invoiceEntity.getId());
        List<InvoiceDetailAD.SeatInvoice> seatInvoices = invoiceEntity.getTickets().stream()
                .map(t -> {InvoiceDetailAD.SeatInvoice seat = new InvoiceDetailAD.SeatInvoice();
                    seat.setId(t.getId());
                    seat.setSeatCode(t.getSeat().getSeatCode());
                    seat.setPrice(t.getTicketPrice().getPrice());
                    return seat;
                }).toList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return InvoiceDetailAD.builder()
                .invoiceId(invoiceEntity.getId())
                .bookingCode(invoiceEntity.getBookingCode())
                .movieName(ticketEntity.getShowTime().getMovie().getName())
                .screenRoomName(ticketEntity.getShowTime().getScreenRoom().getCinema().getName())
                .screenRoomTypeName(ticketEntity.getShowTime().getScreenRoom().getCinema().getCinemaType().getName())
                .cinema(ticketEntity.getShowTime().getScreenRoom().getCinema().getName())
                .showTime(ticketEntity.getShowTime().getStartTime().toString() + "-" +
                        ticketEntity.getShowTime().getEndTime().toString())
                .showDate(ticketEntity.getShowTime().getShowDate().format(formatter))
                .bookDay(invoiceEntity.getCreatedDate().format(formatter))
                .userId(invoiceEntity.getUser().getId())
                .userName(invoiceEntity.getUser().getFirstName() + " " + invoiceEntity.getUser().getLastName())
                .userEmail(invoiceEntity.getUser().getEmail())
                .userPhone(invoiceEntity.getUser().getPhone())
                .status(invoiceEntity.getStatus())
                .totalMoney(invoiceEntity.getTotalAmount())
                .seatList(seatInvoices)
                .build();
    }


    public InvoiceDetailResponse getInvoiceDetail(Invoice invoiceEntity) {
        Ticket ticketEntity = getTicket(invoiceEntity.getId());

        List<String> seatCodeList = ticketRepository.findSeatCodesByInvoiceId(invoiceEntity.getId());
        return InvoiceDetailResponse.builder()
                .bookingCode(invoiceEntity.getBookingCode())
                .movieName(ticketEntity.getShowTime().getMovie().getName())
                .totalMoney(invoiceEntity.getTotalAmount())
                .totalTicket(seatCodeList.size())
                .showDate(ticketEntity.getShowTime().getShowDate())
                .startTime(ticketEntity.getShowTime().getStartTime())
                .screenRoomName(ticketEntity.getShowTime().getScreenRoom().getName())
                .screenRoomType(ticketEntity.getShowTime().getScreenRoom().getScreenRoomType().getName())
                .seatList(seatCodeList)
                .userId(invoiceEntity.getUser().getId())
                .userName(invoiceEntity.getUser().getFirstName() + " " + invoiceEntity.getUser().getLastName())
                .invoiceId(invoiceEntity.getId())
                .build();
    }
}
