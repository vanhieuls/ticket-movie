package com.example.english.Service.Interface;

import com.example.english.Dto.Response.CinemaRevenue;
import com.example.english.Dto.Response.MovieRevenue;
import com.example.english.Dto.Response.RevenueDashboardResponse;
import com.example.english.Dto.Response.StatisticSummary;

import java.time.LocalDate;
import java.util.List;

public interface RevenueStatisticsService {
    RevenueDashboardResponse getDashboardRevenue();
    List<StatisticSummary> getRevenueList();
    List<MovieRevenue> getMovieRevenue(LocalDate startDate, LocalDate endDate);
    List<CinemaRevenue> getCinemaRevenue(LocalDate startDate, LocalDate endDate);
}
