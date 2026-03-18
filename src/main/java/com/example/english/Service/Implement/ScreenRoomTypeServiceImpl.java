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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<ScreenRoomTypeResponse> getAllScreenRoomType(Integer pageNumber, Integer sizeNumber, String sortBy, String sortDir, Boolean status) {
        Pageable pageable= null;
        if (pageNumber == null || pageNumber < 0) pageNumber = 0;
        if (sizeNumber == null || sizeNumber <= 0) sizeNumber = 10;
        String sortField = (sortBy != null) ? sortBy : "id";
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        pageable = PageRequest.of(pageNumber, sizeNumber, Sort.by(direction, sortField));
//        Page<ScreenRoomType> screenRoomTypes = screenRoomTypeRepository.findAll(pageable);
        Page<ScreenRoomType> screenRoomTypes;
        if (status == null) {
            screenRoomTypes = screenRoomTypeRepository.findAll(pageable);
        } else {
            screenRoomTypes = screenRoomTypeRepository.findByStatus(status, pageable);
        }
        return screenRoomTypes.map(screenRoomTypeMapper::toScreenRoomTypeResponse);
    }

    @Override
    public void updateStatusScreenRoomType(Long id) {
        ScreenRoomType screenRoomType = screenRoomTypeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SCREEN_ROOM_TYPE_NOT_EXISTED));
        screenRoomType.setStatus(!screenRoomType.isStatus());
        screenRoomTypeRepository.save(screenRoomType);
    }
}
