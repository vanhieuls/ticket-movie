package com.example.english.Service.ToolFunction;

import com.example.english.Dto.Response.ShowTimeDto;
import com.example.english.Service.Interface.ShowTimeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShowTimeTool {
    private final ShowTimeService showTimeService;

    @Tool(description = """
    Tìm các suất chiếu phim trong hệ thống.
    Dùng khi người dùng hỏi về giờ chiếu / suất chiếu / lịch chiếu.
    """)
    public List<ShowTimeDto> searchShowtimes(
            @ToolParam(description = "Tên phim mà người dùng muốn xem") String movieName,
            @ToolParam(description = "Tên rạp ") String cinemaName,
            @ToolParam(description = "Ngày chiếu dạng yyyy-MM-dd (có thể null nếu không chỉ rõ) ") @Nullable LocalDate showDate
    ){
        LocalDate date = LocalDate.now();
        if (showDate != null)  date = showDate;
        return showTimeService.getShowTimeDto(movieName, cinemaName, date);
    }
}
