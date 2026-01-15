package com.example.english.Service.Interface;

import com.example.english.Dto.Request.MovieRequest;
import com.example.english.Dto.Response.MovieDetailResponse;
import com.example.english.Dto.Response.MovieSummaryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MovieService {
    MovieDetailResponse createMovie(MovieRequest movieRequest, MultipartFile imageFiles);
    MovieDetailResponse updateMovie(Long id , MovieRequest movieRequest, MultipartFile imageFile);
    void deleteMovie(Long id);
    MovieDetailResponse getMovie(Long id);
    void updateMovieActive(Long id);
    List<MovieSummaryResponse> getAllMovies();
    List<MovieSummaryResponse> getNowPlayingMovies();
    List<MovieSummaryResponse> getUpcomingMovies();

}
