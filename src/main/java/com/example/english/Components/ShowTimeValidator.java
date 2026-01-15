package com.example.english.Components;

import com.example.english.Dto.Request.ShowTimeRequest;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Repository.ShowTimeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShowTimeValidator {
    ShowTimeRepository showTimeRepository;
    public void checkOverlap(ShowTimeRequest showTimeRequest, Long showTimeId) {
        boolean overlap = showTimeRepository.existsOverlapping(
                showTimeRequest.getScreenRoomId(),
                showTimeRequest.getShowDate(),
                showTimeRequest.getStartTime(),
                showTimeRequest.getEndTime(),
                showTimeId
        );
        if (overlap) {
            throw new AppException(ErrorCode.SHOW_TIME_OVERLAP);
        }
    }
}

