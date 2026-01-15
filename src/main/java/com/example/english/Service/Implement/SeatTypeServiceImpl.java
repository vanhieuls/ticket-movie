package com.example.english.Service.Implement;

import com.example.english.Dto.Request.SeatTypeRequest;
import com.example.english.Dto.Response.SeatTypeResponse;
import com.example.english.Entity.SeatType;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Mapper.SeatTypeMapper;
import com.example.english.Repository.SeatTypeRepository;
import com.example.english.Service.Interface.SeatTypeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SeatTypeServiceImpl implements SeatTypeService {
    SeatTypeRepository seatTypeRepository;
    SeatTypeMapper seatTypeMapper;
    @Override
    public SeatTypeResponse createSeatType(SeatTypeRequest seatTypeRequest) {
        if(seatTypeRepository.existsByName(seatTypeRequest.getName())){
            throw new AppException(ErrorCode.SEAT_TYPE_EXISTED);
        }
        SeatType seatType = seatTypeMapper.toSeatTypeEntity(seatTypeRequest);
        seatType.setStatus(true);
        SeatType savedSeatType = seatTypeRepository.save(seatType);
        return seatTypeMapper.toSeatTypeResponse(savedSeatType);
    }

    @Override
    public SeatTypeResponse updateSeatType(Long id, SeatTypeRequest seatTypeRequest) {
        SeatType seatType = seatTypeRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.SEAT_TYPE_NOT_FOUND));
        seatTypeMapper.updateSeatType(seatTypeRequest, seatType);
        SeatType updatedSeatType = seatTypeRepository.save(seatType);
        return seatTypeMapper.toSeatTypeResponse(updatedSeatType);
    }

    @Override
    public SeatTypeResponse getSeatType(Long id) {
        SeatType seatType = seatTypeRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.SEAT_TYPE_NOT_FOUND));
        return seatTypeMapper.toSeatTypeResponse(seatType);
    }

    @Override
    public void updateStatusSeatType(Long id) {
        SeatType seatType = seatTypeRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.SEAT_TYPE_NOT_FOUND));
        seatType.setStatus(!seatType.isStatus());
        seatTypeRepository.save(seatType);
    }

    @Override
    public List<SeatTypeResponse> getAllSeatType() {
        List<SeatType> seatTypes = seatTypeRepository.findAll();
        if (seatTypes.isEmpty()) {
            throw new AppException(ErrorCode.SEAT_TYPE_LIST_EMPTY);
        }
        return seatTypeMapper.toSeatTypeResponseList(seatTypes);
    }
}
