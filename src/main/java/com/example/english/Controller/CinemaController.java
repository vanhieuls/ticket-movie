package com.example.english.Controller;

import com.example.english.Dto.Response.ApiResponse;
import com.example.english.Dto.Response.CinemaSummaryResponse;
import com.example.english.Service.Interface.CinemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cinema")
@Slf4j
@Tag(name = "Cinema Controller", description = "APIs for cinema functionalities")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CinemaController {
    CinemaService cinemaService;
    @Operation(summary = "Get Cinemas", description = "API lấy danh sách rạp chiếu phim theo địa chỉ")
    @GetMapping
    public ApiResponse<List<CinemaSummaryResponse>> getCinemas(@RequestParam String address) {
        return ApiResponse.<List<CinemaSummaryResponse>>builder()
                .code(200)
                .message("Get cinemas successfully")
                .result(cinemaService.getCinemas(address))
                .build();
    }
}
