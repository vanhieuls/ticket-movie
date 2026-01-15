package com.example.english.Service.Implement;

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
import com.example.english.Repository.InvoiceRepository;
import com.example.english.Repository.SeatRepository;
import com.example.english.Repository.ShowTimeRepository;
import com.example.english.Repository.UserRepository;
import com.example.english.Service.Interface.InvoiceService;
import com.example.english.Service.Interface.TicketPriceService;
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

    }

    @Override
    public List<InvoiceResponse> getInvoiceList() {
        return List.of();
    }

    @Override
    public InvoiceDetailResponse getInvoice(Long invoiceId) {
        return null;
    }

    @Override
    public InvoiceDetailResponse getInvoiceByBookingCode(String bookingCode) {
        return null;
    }

    @Override
    public void checkInInvoice(Long invoiceId) {

    }

    @Override
    public List<InvoiceSummary> getInvoiceSummaryList() {
        return List.of();
    }

    @Override
    public InvoiceDetailAD getInvoiceDetail(Long id) {
        return null;
    }
}
