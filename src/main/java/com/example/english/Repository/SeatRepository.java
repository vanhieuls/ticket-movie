package com.example.english.Repository;

import com.example.english.Entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByScreenRoom_Id(Long screenRoomId);
    int countByScreenRoom_Id(Long id);
}
