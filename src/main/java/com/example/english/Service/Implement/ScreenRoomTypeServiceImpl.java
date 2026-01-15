package com.example.english.Service.Implement;

import com.example.english.Dto.Request.ScreenRoomTypeRequest;
import com.example.english.Dto.Response.ScreenRoomTypeResponse;
import com.example.english.Entity.ScreenRoomType;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Mapper.ScreenRoomTypeMapper;
import com.example.english.Repository.ScreenRoomTypeRepository;
import com.example.english.Service.Interface.ScreenRoomTypeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ScreenRoomTypeServiceImpl implements ScreenRoomTypeService {
    ScreenRoomTypeRepository screenRoomTypeRepository;
    ScreenRoomTypeMapper screenRoomTypeMapper;
    @Override
    public ScreenRoomTypeResponse createScreenRoomType(ScreenRoomTypeRequest screenRoomTypeRequest) {
        if(screenRoomTypeRepository.existsByName(screenRoomTypeRequest.getName())){
            throw new AppException(ErrorCode.SCREEN_ROOM_TYPE_EXISTED);
        }
        ScreenRoomType screenRoomTyp = screenRoomTypeMapper.toScreenRoomType(screenRoomTypeRequest);
        screenRoomTyp.setStatus(true);
        ScreenRoomType screenRoomType = screenRoomTypeRepository.save(screenRoomTyp);
        return screenRoomTypeMapper.toScreenRoomTypeResponse(screenRoomType);
    }

    @Override
    public ScreenRoomTypeResponse updateScreenRoomType(Long id, ScreenRoomTypeRequest screenRoomTypeRequest) {
        ScreenRoomType screenRoomType = screenRoomTypeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SCREEN_ROOM_TYPE_NOT_EXISTED));
        screenRoomTypeMapper.updateScreenRoomTypeFromRequest(screenRoomTypeRequest, screenRoomType);
        ScreenRoomType updatedScreenRoomType = screenRoomTypeRepository.save(screenRoomType);
        return screenRoomTypeMapper.toScreenRoomTypeResponse(updatedScreenRoomType);
    }

    @Override
    public ScreenRoomTypeResponse getScreenRoomType(Long id) {
        ScreenRoomType screenRoomType = screenRoomTypeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SCREEN_ROOM_TYPE_NOT_EXISTED));
        return screenRoomTypeMapper.toScreenRoomTypeResponse(screenRoomType);
    }

    @Override
    public List<ScreenRoomTypeResponse> getAllScreenRoomType() {
        List<ScreenRoomType> screenRoomTypes = screenRoomTypeRepository.findAll();
        return screenRoomTypeMapper.toScreenRoomTypeResponseList(screenRoomTypes);
    }

    @Override
    public void updateStatusScreenRoomType(Long id) {
        ScreenRoomType screenRoomType = screenRoomTypeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SCREEN_ROOM_TYPE_NOT_EXISTED));
        screenRoomType.setStatus(!screenRoomType.isStatus());
        screenRoomTypeRepository.save(screenRoomType);
    }
}
