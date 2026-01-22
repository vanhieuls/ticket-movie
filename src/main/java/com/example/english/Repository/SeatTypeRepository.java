package com.example.english.Repository;

import com.example.english.Entity.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatTypeRepository extends JpaRepository<SeatType, Long> {
    boolean existsByName(String name);
    Optional<SeatType> findByName(String name);
}
