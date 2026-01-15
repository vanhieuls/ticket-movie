package com.example.english.Mapper;

import com.example.english.Dto.Request.MovieRequest;
import com.example.english.Dto.Response.MovieDetailResponse;
import com.example.english.Dto.Response.MovieSummaryResponse;
import com.example.english.Entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface MovieMapper {
    Movie toMovie(MovieRequest request);
    MovieDetailResponse toMovieDetailResponse(Movie movie);
    void updateMovie(MovieRequest request, @MappingTarget Movie movie);
    List<MovieSummaryResponse> toMovieSummaryResponses(List<Movie> movies);
}
