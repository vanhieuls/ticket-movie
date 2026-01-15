package com.example.english.Mapper;

import com.example.english.Dto.Request.ScreenRoomRequest;
import com.example.english.Dto.Response.ScreenRoomDetailResponse;
import com.example.english.Dto.Response.ScreenRoomResponse;
import com.example.english.Entity.ScreenRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ScreenRoomMapper {

    ScreenRoom toScreenRoom(ScreenRoomRequest screenRoomRequest);
    @Mapping(target = "cinemaId", source = "cinema.id")
    @Mapping(target = "screenRoomTypeId", source = "screenRoomType.id")
    ScreenRoomResponse toScreenRoomResponse(ScreenRoom screenRoomRequest);
    @Mapping(target = "cinemaName", source = "cinema.name")
    @Mapping(target = "screenRoomTypeName", source = "screenRoomType.name")
    @Mapping(
            target = "status",
            expression = "java(Boolean.TRUE.equals(screenRoom.getStatus()) ? \"Đang hoạt động\" : \"Đang bảo trì\")"
    )
    ScreenRoomDetailResponse toScreenRoomDetailResponse(ScreenRoom screenRoom);
    void updateScreenRoomFromRequest(ScreenRoomRequest screenRoomRequest, @MappingTarget ScreenRoom screenRoom);
}
