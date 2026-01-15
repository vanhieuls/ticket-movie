package com.example.english.Repository;

import com.example.english.Entity.ScreenRoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenRoomTypeRepository extends JpaRepository<ScreenRoomType,Long> {
    boolean existsByName(String name);
}
