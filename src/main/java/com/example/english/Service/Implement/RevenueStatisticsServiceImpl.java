package com.example.english.Service.Implement;

import com.example.english.Dto.Response.CinemaRevenue;
import com.example.english.Dto.Response.MovieRevenue;
import com.example.english.Dto.Response.RevenueDashboardResponse;
import com.example.english.Dto.Response.StatisticSummary;
import com.example.english.Entity.Invoice;
import com.example.english.Repository.InvoiceRepository;
import com.example.english.Service.Interface.RevenueStatisticsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RevenueStatisticsServiceImpl implements RevenueStatisticsService {
    InvoiceRepository invoiceRepository;
    @Override
    public RevenueDashboardResponse getDashboardRevenue() {
        List<Invoice> invoiceEntities = invoiceRepository.findAllByMonthAndYear(
                LocalDate.now().getMonthValue(),
                LocalDate.now().getYear()
        );

        int todayTicket = 0;
        int monthTicket = 0;
        BigDecimal todayRevenue = new BigDecimal(0);
        BigDecimal monthRevenue =  new BigDecimal(0);

        RevenueDashboardResponse response = new RevenueDashboardResponse();

        for (Invoice invoiceEntity : invoiceEntities) {
            if(invoiceEntity.getCreatedDate().equals(LocalDate.now())) {
                todayTicket = todayTicket + invoiceEntity.getTickets().size();
                todayRevenue = todayRevenue.add(invoiceEntity.getTotalAmount());
            }
            monthRevenue = monthRevenue.add(invoiceEntity.getTotalAmount());
            monthTicket = monthTicket + invoiceEntity.getTickets().size();
        }

        return RevenueDashboardResponse.builder()
                .todayRevenue(todayRevenue)
                .monthRevenue(monthRevenue)
                .monthTicket(monthTicket)
                .todayTicket(todayTicket)
                .build();
    }

    @Override
    public List<StatisticSummary> getRevenueList() {
        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate endDate = LocalDate.of(LocalDate.now().getYear()+ 1, 1, 1);
        List<Invoice> invoiceEntities = invoiceRepository.findInvoicesInYear(startDate, endDate);
        Map<Integer, StatisticSummary> summaryMap = new HashMap<>();

        for (Invoice invoice : invoiceEntities) {
            int month = invoice.getCreatedDate().getMonthValue();

            StatisticSummary summary = summaryMap.computeIfAbsent(month, m -> {
                StatisticSummary s = new StatisticSummary();
                s.setMonth(m);
                s.setMonthRevenue(BigDecimal.ZERO);
                s.setMonthTicket(0);
                return s;
            });

            int invoiceTickets = invoice.getTickets().size();
            BigDecimal invoiceRevenue = invoice.getTotalAmount();

            summary.setMonthTicket(summary.getMonthTicket() + invoiceTickets);
            summary.setMonthRevenue(summary.getMonthRevenue().add(invoiceRevenue));
        }


        return summaryMap.values().stream()
                .sorted(Comparator.comparingInt(StatisticSummary::getMonth))
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieRevenue> getMovieRevenue(LocalDate startDate, LocalDate endDate) {
        List<Invoice> invoiceEntities = invoiceRepository.findByCreatedDateBetween(startDate, endDate);

        Map<String, MovieRevenue> movieRevenueMap = new HashMap<>();
        for (Invoice invoice : invoiceEntities) {
            String movieName = invoice.getTickets().getFirst().getShowTime().getMovie().getName();

            MovieRevenue movie = movieRevenueMap.computeIfAbsent(movieName, mn -> {
                MovieRevenue mr = new MovieRevenue();
                mr.setMovieName(mn);
                mr.setTotalRevenue(BigDecimal.ZERO);
                mr.setTotalTicket(0);
                return mr;
            });

            int movieTickets = invoice.getTickets().size();
            BigDecimal movieRevenue = invoice.getTotalAmount();

            movie.setTotalRevenue(movie.getTotalRevenue().add(movieRevenue));
            movie.setTotalTicket(movie.getTotalTicket() + movieTickets);
        }

        return new ArrayList<>(movieRevenueMap.values());
    }

    @Override
    public List<CinemaRevenue> getCinemaRevenue(LocalDate startDate, LocalDate endDate) {
        List<Invoice> invoiceEntities = invoiceRepository.findByCreatedDateBetween(startDate, endDate);

        Map<String, CinemaRevenue> cinemaRevenueMap = new HashMap<>();
        for (Invoice invoice : invoiceEntities) {
            String cinemaName = invoice.getTickets().getFirst().getShowTime().getScreenRoom().getCinema().getName();

            CinemaRevenue cinema = cinemaRevenueMap.computeIfAbsent(cinemaName, cn -> {
                CinemaRevenue mr = new CinemaRevenue();
                mr.setCinemaName(cn);
                mr.setTotalRevenue(BigDecimal.ZERO);
                mr.setTotalTicket(0);
                return mr;
            });

            int cinemaTickets = invoice.getTickets().size();
            BigDecimal cinemaRevenue = invoice.getTotalAmount();

            cinema.setTotalRevenue(cinema.getTotalRevenue().add(cinemaRevenue));
            cinema.setTotalTicket(cinema.getTotalTicket() + cinemaTickets);
        }

        return new ArrayList<>(cinemaRevenueMap.values());
    }
}
