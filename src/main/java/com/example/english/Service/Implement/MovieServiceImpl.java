package com.example.english.Service.Implement;

import com.cloudinary.Cloudinary;
import com.example.english.Configuration.CommonConfig;
import com.example.english.Dto.Request.MovieRequest;
import com.example.english.Dto.Response.MovieDetailResponse;
import com.example.english.Dto.Response.MovieSummaryResponse;
import com.example.english.Entity.Movie;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Mapper.MovieMapper;
import com.example.english.Repository.MovieRepository;
import com.example.english.Service.Interface.MovieService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MovieServiceImpl implements MovieService {
    MovieRepository movieRepository;
    CommonConfig commonConfig;
    Cloudinary cloudinary;
    MovieMapper movieMapper;

    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;

    private void validatePoster(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new AppException(ErrorCode.FILE_TOO_LARGE);
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new AppException(ErrorCode.UNSUPPORTED_FILE_TYPE);
        }

        String filename = file.getOriginalFilename();
        if (filename != null) {
            String lower = filename.toLowerCase();
            if (!(lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png") || lower.endsWith(".webp"))) {
                throw new AppException(ErrorCode.UNSUPPORTED_FILE_TYPE);
            }
        }
    }

    @Override
    public MovieDetailResponse createMovie(MovieRequest movieRequest, MultipartFile imageFiles) {
        if(movieRepository.existsByName(movieRequest.getName())){
            throw new AppException(ErrorCode.MOVIE_ALREADY_EXISTS);
        }
        if(movieRequest.getReleaseDate().isAfter(movieRequest.getEndDate())){
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }
        if(imageFiles.isEmpty()){
            throw new AppException(ErrorCode.FILE_REQUIRED);
        }
        validatePoster(imageFiles);

        try {
            Map data = this.cloudinary.uploader().upload(imageFiles.getBytes(), Map.of());
            Movie movie = movieMapper.toMovie(movieRequest);
            movie.setPosterUrl(data.get("secure_url").toString());
            Movie savedMovie = movieRepository.save(movie);
            return movieMapper.toMovieDetailResponse(savedMovie);
        }
        catch (IOException | DataIntegrityViolationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public MovieDetailResponse updateMovie(Long id, MovieRequest movieRequest, MultipartFile imageFiles) {
        Movie movie = movieRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.MOVIE_NOT_FOUND));
        if(movieRequest.getReleaseDate().isAfter(movieRequest.getEndDate())){
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }
        if(imageFiles!=null && !imageFiles.isEmpty()){
            validatePoster(imageFiles);
            try {
                Map data = this.cloudinary.uploader().upload(imageFiles.getBytes(), Map.of());
                movieMapper.updateMovie(movieRequest, movie);
                movie.setPosterUrl(data.get("secure_url").toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            movieMapper.updateMovie(movieRequest, movie);
            movie.setPosterUrl(movie.getPosterUrl());
        }
        try{
            movieRepository.save(movie);
            return movieMapper.toMovieDetailResponse(movie);
        }
        catch (DataIntegrityViolationException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.MOVIE_NOT_FOUND));
        movie.setStatus(false);
        movieRepository.save(movie);
    }

    @Override
    public MovieDetailResponse getMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.MOVIE_NOT_FOUND));
        return movieMapper.toMovieDetailResponse(movie);
    }

    @Override
    public void updateMovieActive(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.MOVIE_NOT_FOUND));
        movie.setStatus(true);
        movieRepository.save(movie);
    }

    @Override
    public List<MovieSummaryResponse> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        if(movies.isEmpty()){
            throw new AppException(ErrorCode.MOVIE_NOT_FOUND);
        }
        return movieMapper.toMovieSummaryResponses(movies);
    }

    @Override
    public List<MovieSummaryResponse> getNowPlayingMovies() {
        List<Movie> movies = movieRepository.findNowPlayingMovies(LocalDate.now());
        if(movies.isEmpty()){
            throw new AppException(ErrorCode.MOVIE_NOT_FOUND);
        }
        else{
            return movieMapper.toMovieSummaryResponses(movies);
        }
    }

    @Override
    public List<MovieSummaryResponse> getUpcomingMovies() {
        List<Movie> movies = movieRepository.findUpcomingMovies(LocalDate.now());
        if(movies.isEmpty()){
            throw new AppException(ErrorCode.MOVIE_NOT_FOUND);
        }
        else{
            return movieMapper.toMovieSummaryResponses(movies);
        }
    }
}
