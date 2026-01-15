package com.example.english.Repository;

import com.example.english.Entity.SpecialDay;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialDayRepository extends JpaRepository<SpecialDay,Long> {
    boolean existsByMonthAndDay(int month, int day);
}
