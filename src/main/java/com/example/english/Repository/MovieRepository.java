package com.example.english.Repository;

import com.example.english.Entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByName(String name);
    boolean existsByName(String name);
    @Query("SELECT m FROM Movie m WHERE m.releaseDate <= :today AND m.endDate >= :today AND m.status IS TRUE ")
    List<Movie> findNowPlayingMovies(@Param("today") LocalDate today);

    @Query("SELECT m FROM Movie m WHERE m.releaseDate > :today AND m.status IS TRUE ")
    List<Movie> findUpcomingMovies(@Param("today") LocalDate today);
}
