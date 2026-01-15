package com.example.english.Repository;

import com.example.english.Entity.TicketPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface TicketPriceRepository extends JpaRepository<TicketPrice, Long> {
    boolean existsBySpecialDayAndTimeFrameAndCinemaType_IdAndScreenRoomType_IdAndSeatType_Id(
            Boolean specialDay,
            String timeFrame,
            Long cinemaTypeId,
            Long screenRoomTypeId,
            Long seatTypeId);
    Optional<TicketPrice> findBySpecialDayAndTimeFrameAndCinemaType_IdAndScreenRoomType_IdAndSeatType_Id(
            Boolean specialDay,
            String timeFrame,
            Long cinemaTypeId,
            Long screenRoomTypeId,
            Long seatTypeId);
}
