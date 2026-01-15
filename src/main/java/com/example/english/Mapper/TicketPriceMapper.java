package com.example.english.Mapper;

import com.example.english.Dto.Response.TicketPriceResponse;
import com.example.english.Entity.TicketPrice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TicketPriceMapper {
    @Mapping(source = "cinemaType.name", target = "cinemaType")
    @Mapping(source = "screenRoomType.name", target = "screenRoomType")
    @Mapping(source = "seatType.name", target = "seatType")
    @Mapping(target = "dayType", expression = "java(ticketPrice.isSpecialDay() ? \"Ngày lễ, Cuối tuần\" : \"Ngày thường\")")
    TicketPriceResponse toTicketPriceResponse(TicketPrice ticketPrice);
    List<TicketPriceResponse> toTicketPriceResponses(List<TicketPrice> ticketPrices);
}
