package com.example.english.Service.Interface;

import com.example.english.Dto.Request.ScreenRoomTypeRequest;
import com.example.english.Dto.Response.ScreenRoomTypeResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ScreenRoomTypeService {
    ScreenRoomTypeResponse createScreenRoomType(ScreenRoomTypeRequest screenRoomTypeRequest);
    ScreenRoomTypeResponse updateScreenRoomType(Long id, ScreenRoomTypeRequest screenRoomTypeRequest);
    ScreenRoomTypeResponse getScreenRoomType(Long id);
    Page<ScreenRoomTypeResponse> getAllScreenRoomType(Integer pageNumber, Integer sizeNumber, String sortBy, String sortDir, Boolean status);
    void updateStatusScreenRoomType(Long id);
}
