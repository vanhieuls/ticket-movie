package com.example.english.Service.Interface;

import com.example.english.Dto.Request.ScreenRoomTypeRequest;
import com.example.english.Dto.Response.ScreenRoomTypeResponse;

import java.util.List;

public interface ScreenRoomTypeService {
    ScreenRoomTypeResponse createScreenRoomType(ScreenRoomTypeRequest screenRoomTypeRequest);
    ScreenRoomTypeResponse updateScreenRoomType(Long id, ScreenRoomTypeRequest screenRoomTypeRequest);
    ScreenRoomTypeResponse getScreenRoomType(Long id);
    List<ScreenRoomTypeResponse> getAllScreenRoomType();
    void updateStatusScreenRoomType(Long id);
}
