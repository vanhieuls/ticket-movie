package com.example.english.Service.Implement;

import com.example.english.Dto.Request.ScreenRoomRequest;
import com.example.english.Dto.Response.ScreenRoomDetailResponse;
import com.example.english.Dto.Response.ScreenRoomResponse;
import com.example.english.Entity.Cinema;
import com.example.english.Entity.ScreenRoom;
import com.example.english.Entity.ScreenRoomType;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Mapper.ScreenRoomMapper;
import com.example.english.Repository.CinemaRepository;
import com.example.english.Repository.ScreenRoomRepository;
import com.example.english.Repository.ScreenRoomTypeRepository;
import com.example.english.Service.Interface.ScreenRoomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScreenRoomServiceImpl implements ScreenRoomService {
    ScreenRoomRepository screenRoomRepository;
    ScreenRoomMapper screenRoomMapper;
    ScreenRoomTypeRepository screenRoomTypeRepository;
    CinemaRepository cinemaRepository;
    @Override
    public ScreenRoomResponse createScreenRoom(ScreenRoomRequest screenRoomRequest) {
        if(screenRoomRepository.existsByNameAndCinemaIdAndScreenRoomTypeId(
                screenRoomRequest.getName(),
                screenRoomRequest.getCinemaId(),
                screenRoomRequest.getScreenRoomTypeId()
        )){
            throw  new AppException(ErrorCode.SCREEN_ROOM_EXISTED);
        }
        Cinema cinema = cinemaRepository.findById(screenRoomRequest.getCinemaId())
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_NOT_EXISTED));
        ScreenRoomType screenRoomType = screenRoomTypeRepository.findById(screenRoomRequest.getScreenRoomTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.SCREEN_ROOM_TYPE_NOT_EXISTED));
        ScreenRoom screenRoom = screenRoomMapper.toScreenRoom(screenRoomRequest);
        screenRoom.setScreenRoomType(screenRoomType);
        screenRoom.setCinema(cinema);
        screenRoomRepository.save(screenRoom);
        return screenRoomMapper.toScreenRoomResponse(screenRoom);
    }

    @Override
    public ScreenRoomResponse updateScreenRoom(Long id, ScreenRoomRequest screenRoomRequest) {
        ScreenRoom screenRoomEntity = screenRoomRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SCREEN_ROOM_NOT_EXISTED));
        if(screenRoomRepository.existsByNameAndCinemaIdAndScreenRoomTypeId(
                screenRoomRequest.getName(),
                screenRoomRequest.getCinemaId(),
                screenRoomRequest.getScreenRoomTypeId()
        )){
            throw  new AppException(ErrorCode.SCREEN_ROOM_EXISTED);
        }
        Cinema cinema = cinemaRepository.findById(screenRoomRequest.getCinemaId())
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_NOT_EXISTED));
        ScreenRoomType screenRoomType = screenRoomTypeRepository.findById(screenRoomRequest.getScreenRoomTypeId())
                .orElseThrow(() -> new AppException(ErrorCode.SCREEN_ROOM_TYPE_NOT_EXISTED));
        screenRoomMapper.updateScreenRoomFromRequest(screenRoomRequest, screenRoomEntity);
        screenRoomEntity.setScreenRoomType(screenRoomType);
        screenRoomEntity.setCinema(cinema);
        screenRoomRepository.save(screenRoomEntity);
        return screenRoomMapper.toScreenRoomResponse(screenRoomEntity);
    }

    @Override
    public List<ScreenRoomDetailResponse> getScreenRoomList(Long id) {
        return screenRoomRepository.findByCinemaId(id).stream()
                .map(
                        screenRoomMapper::toScreenRoomDetailResponse
                )
                .collect(Collectors.toList());
    }

    @Override
    public ScreenRoomResponse getScreenRoomDetail(Long id) {
        return screenRoomRepository.findById(id)
                .map(screenRoomMapper::toScreenRoomResponse)
                .orElseThrow(() -> new AppException(ErrorCode.SCREEN_ROOM_NOT_EXISTED));
    }

    @Override
    public void deleteScreenRoom(Long id) {
        ScreenRoom screenRoom = screenRoomRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SCREEN_ROOM_NOT_EXISTED));
        screenRoom.setStatus(false);
        screenRoomRepository.save(screenRoom);
    }

    @Override
    public List<ScreenRoomDetailResponse> getScreenRoomActiveList(Long id) {
        return screenRoomRepository.findByCinemaIdAndStatusTrue(id).stream()
                .map(
                        screenRoomMapper::toScreenRoomDetailResponse
                )
                .collect(Collectors.toList());
    }
}
