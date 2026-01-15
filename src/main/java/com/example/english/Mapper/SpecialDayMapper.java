package com.example.english.Mapper;

import com.example.english.Dto.Request.SpecialDayRequest;
import com.example.english.Entity.SpecialDay;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecialDayMapper {
    SpecialDay toSpecialDay(SpecialDayRequest specialDayRequest);
    void updateSpecialDay(@MappingTarget SpecialDay specialDay, SpecialDayRequest specialDayRequest);
}
