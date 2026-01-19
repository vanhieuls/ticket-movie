package com.example.english.Service.Interface;

import com.example.english.Dto.Request.FilterMovie;
import com.example.english.Dto.Request.MovieRequest;
import com.example.english.Dto.Response.MovieDetailResponse;
import com.example.english.Dto.Response.MovieResponse;
import com.example.english.Dto.Response.MovieSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface MovieService {
    MovieDetailResponse createMovie(MovieRequest movieRequest, MultipartFile imageFiles);
    MovieDetailResponse updateMovie(Long id , MovieRequest movieRequest, MultipartFile imageFile);
    void deleteMovie(Long id);
    MovieDetailResponse getMovie(Long id);
    void updateMovieActive(Long id);
    Page<MovieSummaryResponse> getAllMovies(Integer pageNumber, Integer pageSize);
    List<MovieSummaryResponse> getNowPlayingMovies();
    List<MovieSummaryResponse> getUpcomingMovies();
    List<String> getListCinemaAddress(FilterMovie filterMovie);
    Page<MovieSummaryResponse> getFilterMovie (Integer pageNumber, Integer pageSize,String category, String brand, String properties,String sortDir,
                                       BigDecimal minPrice,
                                       BigDecimal maxPrice);
    List<MovieResponse> getMovieShowDay(LocalDate date);
}
