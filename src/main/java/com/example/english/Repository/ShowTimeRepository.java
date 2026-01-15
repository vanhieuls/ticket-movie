package com.example.english.Repository;

import com.example.english.Entity.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, Long> {
    @Query("""
        select case when count(s) > 0 then true else false end
        from ShowTime s
        where s.screenRoom.id = :screenRoomId
          and s.showDate = :showDate
          and (:excludeId is null or s.id <> :excludeId)
          and s.startTime < :newEnd
          and s.endTime   > :newStart
    """)
    boolean existsOverlapping(
            @Param("screenRoomId") Long screenRoomId,
            @Param("showDate") LocalDate showDate,
            @Param("newStart") LocalTime newStart,
            @Param("newEnd") LocalTime newEnd,
            @Param("excludeId") Long excludeId //kiểm tra khi update (<> dấu phép so sánh khác nhau)
    ); //mệnh đề WHERE chỉ giữ lại những hàng có điều kiện đánh giá là TRUE.
    // Nếu bất kỳ một điều kiện trong chuỗi AND cho ra FALSE hoặc UNKNOWN (do NULL) thì cả hàng đó bị loại.
    @Query("""
        SELECT DISTINCT s
        FROM ShowTime s
        LEFT JOIN FETCH s.screenRoom sr
        LEFT JOIN FETCH sr.cinema c
        LEFT JOIN FETCH s.movie m
        WHERE c.id = :cinemaId
          AND s.showDate = :date
          AND s.startTime > :currentTime
          AND s.status = true
    """)
    List<ShowTime> findByScreenRoom_Cinema_IdAndShowDateAndStartTimeAfterAndStatusTrue(
            @Param("cinemaId") Long cinemaId,
            @Param("date") LocalDate date,
            @Param("currentTime") LocalTime currentTime
    );
    @Query("""
        SELECT DISTINCT s
        FROM ShowTime s
        LEFT JOIN FETCH s.screenRoom sr
        LEFT JOIN FETCH sr.cinema c
        LEFT JOIN FETCH s.movie m
        WHERE m.id = :movieId
          AND s.showDate = :date
          AND s.startTime > :currentTime
          AND s.status = true
    """)
    List<ShowTime> findByMovie_IdAndShowDateAndStartTimeAfterAndStatusTrue(
            @Param("movieId") Long movieId,
            @Param("date") LocalDate date,
            @Param("currentTime") LocalTime currentTime
    );

    @Query("""
        SELECT DISTINCT s
        FROM ShowTime s
        LEFT JOIN FETCH s.screenRoom sr
        LEFT JOIN FETCH sr.cinema c
        LEFT JOIN FETCH s.movie m
        WHERE c.address = :address
          AND m.id = :movieId
          AND s.showDate = :date
          AND s.startTime > :currentTime
          AND s.status = true
    """)
    List<ShowTime> findByScreenRoom_Cinema_AddressAndMovie_IdAndShowDateAndStartTimeAfterAndStatusTrue(
            String address,
            Long movieId,
            LocalDate date,
            LocalTime currentTime
    );

    @Query("""
        SELECT DISTINCT s
        FROM ShowTime s
        LEFT JOIN FETCH s.screenRoom sr
        LEFT JOIN FETCH sr.cinema c
        LEFT JOIN FETCH s.movie m
        WHERE m.name = :movieName
          AND c.name = :cinemaName
          AND s.showDate = :date
          AND s.status = true
    """)
    List<ShowTime> findByMovie_NameAndScreenRoom_Cinema_NameAndShowDateAndStatusTrue(
            String movieName,
            String cinemaName,
            LocalDate date
    );

    @Query("""
        SELECT s
        FROM ShowTime s
        JOIN FETCH s.screenRoom sr
        WHERE sr.id = :screenRoomId
          AND s.showDate = :showDate
    """)
    List<ShowTime> findByScreenRoom_IdAndShowDate(Long screenRoomId, LocalDate showDate);

}
