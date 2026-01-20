package com.example.english.Mapper;

import com.example.english.Dto.Request.SeatTypeRequest;
import com.example.english.Dto.Response.SeatTypeResponse;
import com.example.english.Entity.SeatType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SeatTypeMapper {
    SeatType toSeatTypeEntity (SeatTypeRequest seatTypeRequest);
    @Mapping(target = "status", expression = "java(seatType.isStatus() ? \"Đang hoạt động\" : \"Tạm ngưng hoạt động\")")
    SeatTypeResponse toSeatTypeResponse (SeatType seatType);
    void updateSeatType(SeatTypeRequest request, @MappingTarget SeatType seatType);
    List<SeatTypeResponse> toSeatTypeResponseList (List<SeatType> seatTypes);
}
