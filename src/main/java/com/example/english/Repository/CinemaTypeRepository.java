package com.example.english.Repository;

import com.example.english.Entity.CinemaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CinemaTypeRepository extends JpaRepository<CinemaType, Long> {
    Optional<Boolean> findByName(String name);
}
