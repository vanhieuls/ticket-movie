package com.example.english.Service.Implement;

import com.example.english.Dto.Request.SpecialDayRequest;
import com.example.english.Entity.SpecialDay;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Mapper.SpecialDayMapper;
import com.example.english.Repository.SpecialDayRepository;
import com.example.english.Service.Interface.SpecialDayService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpecialDayServiceImpl implements SpecialDayService {

    SpecialDayRepository specialDayRepository;
    SpecialDayMapper specialDayMapper;

    @Override
    public SpecialDay createSpecialDay(SpecialDayRequest specialDayRequest) {
        if(specialDayRepository.existsByMonthAndDay(specialDayRequest.getMonth(), specialDayRequest.getDay())){
            throw new AppException(ErrorCode.EXIST_SPECIAL_DAY);
        }
        return specialDayRepository.save(
                specialDayMapper.toSpecialDay(specialDayRequest)
        );
    }

    @Override
    public void deleteSpecialDay(Long id) {
        SpecialDay specialDay = specialDayRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SPECIAL_DAY_NOT_FOUND));
        specialDayRepository.deleteById(specialDay.getId());
    }

    @Override
    public SpecialDay updateSpecialDay(Long id, SpecialDayRequest specialDayRequest) {
        SpecialDay specialDay = specialDayRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SPECIAL_DAY_NOT_FOUND));
        specialDayMapper.updateSpecialDay(specialDay, specialDayRequest);
        return specialDayRepository.save(specialDay);
    }

    @Override
    public SpecialDay getSpecialDayById(Long id) {
        return  specialDayRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SPECIAL_DAY_NOT_FOUND));
    }

    @Override
    public List<SpecialDay> getAllSpecialDays() {
        return specialDayRepository.findAll();
    }
}
