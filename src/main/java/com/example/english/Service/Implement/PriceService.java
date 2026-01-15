package com.example.english.Service.Implement;

import com.example.english.Enum.TimeFrame;
import com.example.english.Repository.SpecialDayRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PriceService {
    SpecialDayRepository specialDayRepository;
    static double  base_price = 100000.0;
    public Double finalPrice(Double priceFactorCinemaType,
                             Double priceFactorScreenRoomType,
                             Double priceFactorSeatType,
                             Boolean specialDay,
                             TimeFrame timeFrame
    )
    {
        double priceBase = base_price;
        //giá theo rạp
        priceBase *= priceFactorCinemaType;

        //giá theo loại ghế
        priceBase *= priceFactorScreenRoomType;

        priceBase *= priceFactorSeatType;

        //giá theo khung giờ
        priceBase *= timeFrame.getPriceFactor();

        if(specialDay){
            priceBase *= 1.1;
        }

        return priceBase;
    }

    public boolean isWeekend(LocalDate showDate) {
        DayOfWeek dayOfWeek = showDate.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SUNDAY || dayOfWeek == DayOfWeek.SATURDAY;
    }
    public boolean isSpecialDay(LocalDate showDate){
        int month = showDate.getMonthValue();
        int day = showDate.getDayOfMonth();
        return specialDayRepository.existsByMonthAndDay(month, day);
    }
}
