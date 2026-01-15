package com.example.english.Service.Interface;

import com.example.english.Dto.Request.SpecialDayRequest;
import com.example.english.Entity.SpecialDay;

import java.util.List;

public interface SpecialDayService {
    SpecialDay createSpecialDay(SpecialDayRequest specialDayRequest);
    void deleteSpecialDay(Long id);
    SpecialDay updateSpecialDay(Long id, SpecialDayRequest specialDayRequest);
    SpecialDay getSpecialDayById(Long id);
    List<SpecialDay> getAllSpecialDays();
}
