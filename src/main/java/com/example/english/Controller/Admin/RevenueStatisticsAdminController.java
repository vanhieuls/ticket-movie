package com.example.english.Controller.Admin;

import com.example.english.Dto.Response.*;
import com.example.english.Service.Interface.RevenueStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/revenue")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Revenue Statistics Admin Controller", description = "APIs thống kê doanh thu cho admin")
public class RevenueStatisticsAdminController {
    RevenueStatisticsService revenueStatisticsService;

    @Operation(summary = "Lấy thông tin doanh thu tổng quan")
    @GetMapping("/dashbroad")
    public ApiResponse<RevenueDashboardResponse> getDashboardRevenue(){
        return ApiResponse.<RevenueDashboardResponse>builder()
                .result(revenueStatisticsService.getDashboardRevenue())
                .build();
    }

    @Operation(summary = "Lấy danh sách thống kê doanh thu theo tháng trong năm")
    @GetMapping("/statistics/list")
    public ApiResponse<List<StatisticSummary>> getRevenueList(){
        return ApiResponse.<List<StatisticSummary>>builder()
                .result(revenueStatisticsService.getRevenueList())
                .build();
    }

    @Operation(summary = "Lấy thống kê doanh thu theo phim trong khoảng thời gian")
    @GetMapping("/movie")
    public ApiResponse<List<MovieRevenue>> getMovieRevenue(@RequestParam("startDay")
                                                    @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDay,
                                                           @RequestParam("endDay")
                                                    @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDay){
        return ApiResponse.<List<MovieRevenue>>builder()
                .result(revenueStatisticsService.getMovieRevenue(startDay, endDay))
                .build();
    }

    @Operation(summary = "Lấy thống kê doanh thu theo rạp trong khoảng thời gian")
    @GetMapping("/cinema")
    public ApiResponse<List<CinemaRevenue>> getCinemaRevenue(@RequestParam("startDay")
                                                      @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDay,
                                                             @RequestParam("endDay")
                                                      @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDay){
        return ApiResponse.<List<CinemaRevenue>>builder()
                .result(revenueStatisticsService.getCinemaRevenue(startDay, endDay))
                .build();
    }
}
