package com.example.english.Repository;

import com.example.english.Entity.ScreenRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreenRoomRepository extends JpaRepository<ScreenRoom,Long> {
    boolean existsByNameAndCinemaIdAndScreenRoomTypeId(String name, Long cinemaId, Long screenRoomTypeId);
//    @EntityGraph(attributePaths = "screenRoomType")
//    @Query("""
//select sr
//from ScreenRoomEntity sr
//where sr.cinema.id = :cinemaId
//""")
    @Query("""
            select sr from ScreenRoom sr
            join fetch ScreenRoomType srt
            Where sr.cinema.id = :cinemaId
            """)
    List<ScreenRoom> findByCinemaIdType(@Param("cinemaId") Long cinemaId);
    List<ScreenRoom> findByCinemaIdAndStatusTrue(Long cinemaId);

}
