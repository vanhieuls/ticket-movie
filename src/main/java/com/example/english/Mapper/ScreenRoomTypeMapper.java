package com.example.english.Mapper;

import com.example.english.Dto.Request.ScreenRoomTypeRequest;
import com.example.english.Dto.Response.ScreenRoomTypeResponse;
import com.example.english.Entity.ScreenRoomType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScreenRoomTypeMapper {
    ScreenRoomType toScreenRoomType(ScreenRoomTypeRequest screenRoomType);
    @Mapping(target = "status", expression = "java(screenRoomType.isStatus()? \"Đang hoạt động\" : \"Tạm ngưng hoạt động\")")
    ScreenRoomTypeResponse toScreenRoomTypeResponse(ScreenRoomType screenRoomType);
    void updateScreenRoomTypeFromRequest(ScreenRoomTypeRequest screenRoomTypeRequest, @MappingTarget ScreenRoomType screenRoomType);
    List<ScreenRoomTypeResponse> toScreenRoomTypeResponseList(List<ScreenRoomType> screenRoomTypes);
}
