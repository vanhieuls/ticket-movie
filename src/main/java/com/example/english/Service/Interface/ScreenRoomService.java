package com.example.english.Service.Interface;

import com.example.english.Dto.Request.ScreenRoomRequest;
import com.example.english.Dto.Response.ScreenRoomDetailResponse;
import com.example.english.Dto.Response.ScreenRoomResponse;

import java.util.List;

public interface ScreenRoomService {
    ScreenRoomResponse createScreenRoom(ScreenRoomRequest screenRoomRequest);
    ScreenRoomResponse updateScreenRoom(Long id, ScreenRoomRequest screenRoomRequest);
    List<ScreenRoomDetailResponse> getScreenRoomList(Long id);
    ScreenRoomResponse getScreenRoomDetail(Long id);
    void deleteScreenRoom(Long id);
    List<ScreenRoomDetailResponse> getScreenRoomActiveList(Long id);
}
