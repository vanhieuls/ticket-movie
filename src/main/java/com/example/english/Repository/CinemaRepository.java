package com.example.english.Repository;

import com.example.english.Entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    Optional<Boolean> findByIdAndStatus(Long id, Boolean status);
    boolean existsByName(String name);
    Optional<List<Cinema>> findByAddressAndStatusTrue(String address);
}
