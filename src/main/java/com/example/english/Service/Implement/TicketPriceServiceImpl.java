package com.example.english.Service.Implement;

import com.example.english.Dto.Request.TicketPriceRequest;
import com.example.english.Dto.Response.TicketPriceResponse;
import com.example.english.Entity.*;
import com.example.english.Enum.TimeFrame;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Mapper.TicketPriceMapper;
import com.example.english.Repository.*;
import com.example.english.Service.Interface.TicketPriceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketPriceServiceImpl implements TicketPriceService {
    TicketPriceRepository ticketPriceRepository;
    CinemaTypeRepository cinemaTypeRepository;
    ScreenRoomTypeRepository screenRoomTypeRepository;
    SeatTypeRepository seatTypeRepository;
    PriceService priceService;
    TicketPriceMapper ticketPriceMapper;

    @Override
    public TicketPriceResponse createTicketPrice(TicketPriceRequest ticketPriceRequest) {
        boolean specialDay = ticketPriceRequest.getDayType().equals("SpecialDay");
        if(ticketPriceRepository.existsBySpecialDayAndTimeFrameAndCinemaType_IdAndScreenRoomType_IdAndSeatType_Id(
                specialDay,
                ticketPriceRequest.getTimeFrame(),
                ticketPriceRequest.getCinemaTypeId(),
                ticketPriceRequest.getScreenRoomTypeId(),
                ticketPriceRequest.getSeatTypeId()
        )){
            throw  new AppException(ErrorCode.TICKET_PRICE_EXISTED);// Handle the case when the ticket price already exists
        }
        CinemaType cinema = cinemaTypeRepository.findById(ticketPriceRequest.getCinemaTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_TYPE_NOT_EXISTED));
        ScreenRoomType screenRoomType = screenRoomTypeRepository.findById(ticketPriceRequest.getScreenRoomTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.SCREEN_ROOM_TYPE_NOT_EXISTED));
        SeatType seatType = seatTypeRepository.findById(ticketPriceRequest.getSeatTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.SEAT_TYPE_NOT_FOUND));
        TimeFrame timeFrame = TimeFrame.fromLabel(ticketPriceRequest.getTimeFrame());
        // Calculate price logic should be here
        Double price = priceService.finalPrice(
                cinema.getPriceFactor(),
                screenRoomType.getPriceFactor(),
                seatType.getPriceFactor(),
                specialDay,
                timeFrame
        );
        TicketPrice ticketPrice = TicketPrice.builder()
                .price(BigDecimal.valueOf(price))
                .cinemaType(cinema)
                .screenRoomType(screenRoomType)
                .seatType(seatType)
                .specialDay(specialDay)
                .timeFrame(timeFrame.getTimeFrame())
                .build();
        TicketPrice savedTicketPrice = ticketPriceRepository.save(ticketPrice);
        return ticketPriceMapper.toTicketPriceResponse(savedTicketPrice);
    }

    @Override
    public TicketPrice getSeatTicketPrice(Long cinemaTypeId,
                                          Long screenRoomTypeId,
                                          Long seatTypeId,
                                          LocalDate date,
                                          LocalTime time) {
        boolean specialDay = false;
        if(priceService.isSpecialDay(date) || priceService.isWeekend(date)){
            specialDay = true;
        }
        TimeFrame timeFrame = TimeFrame.from(time);
        String frame = timeFrame.getTimeFrame();
        TicketPrice ticketPrice = ticketPriceRepository
                .findBySpecialDayAndTimeFrameAndCinemaType_IdAndScreenRoomType_IdAndSeatType_Id(
                        specialDay,
                        frame,
                        cinemaTypeId,
                        screenRoomTypeId,
                        seatTypeId
                ).orElseThrow(() -> new AppException(ErrorCode.TICKET_PRICE_NOT_EXISTED));

        return ticketPrice;
    }

    @Override
    public TicketPriceResponse updateTicketPrice(Long id, TicketPriceRequest ticketPriceRequest) {
        TicketPrice existingTicketPrice = ticketPriceRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_PRICE_NOT_EXISTED));

        boolean specialDay = ticketPriceRequest.getDayType().equals("SpecialDay");

        CinemaType cinemaType = cinemaTypeRepository.findById(ticketPriceRequest.getCinemaTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_TYPE_NOT_EXISTED));

        ScreenRoomType screenRoomType = screenRoomTypeRepository.findById(ticketPriceRequest.getScreenRoomTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.SCREEN_ROOM_TYPE_NOT_EXISTED));

        SeatType seatType = seatTypeRepository.findById(ticketPriceRequest.getSeatTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.SEAT_TYPE_NOT_FOUND));

        TimeFrame timeFrame = TimeFrame.fromLabel(ticketPriceRequest.getTimeFrame());

        Double price = priceService.finalPrice(
                cinemaType.getPriceFactor(),
                screenRoomType.getPriceFactor(),
                seatType.getPriceFactor(),
                specialDay,
                timeFrame
        );

        existingTicketPrice.setCinemaType(cinemaType);
        existingTicketPrice.setScreenRoomType(screenRoomType);
        existingTicketPrice.setSeatType(seatType);
        existingTicketPrice.setSpecialDay(specialDay);
        existingTicketPrice.setTimeFrame(ticketPriceRequest.getTimeFrame());
        existingTicketPrice.setPrice(BigDecimal.valueOf(price));

        TicketPrice updatedTicketPrice = ticketPriceRepository.save(existingTicketPrice);
        return ticketPriceMapper.toTicketPriceResponse(updatedTicketPrice);
    }

    @Override
    public List<TicketPriceResponse> getAllTicketPrice() {
        List<TicketPrice> ticketPrice =  ticketPriceRepository.findAll();
        return ticketPriceMapper.toTicketPriceResponses(ticketPrice);
    }
}
