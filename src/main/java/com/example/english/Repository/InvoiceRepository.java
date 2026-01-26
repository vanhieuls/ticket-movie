package com.example.english.Repository;

import com.example.english.Entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
    Optional<Invoice> findByTxnRef(String txnRef);
    List<Invoice> findByUser_Id(Long userId);
    List<Invoice> findByCreatedDateBetween(LocalDate startDate, LocalDate endDate);
    Optional<Invoice> findByBookingCode(String bookingCode);
    @Query("""
        SELECT i FROM Invoice i
        WHERE MONTH(i.createdDate) = :month
          AND YEAR(i.createdDate)  = :year
    """)
    List<Invoice> findAllByMonthAndYear(
            @Param("month") int month,
            @Param("year") int year
    );
//    @Query("""
//    SELECT i FROM Invoice i
//    WHERE i.createdDate >= :startDate
//      AND i.createdDate <  :endDate
//""")
//    List<Invoice> findAllByMonthRange(
//            @Param("startDate") LocalDateTime startDate,
//            @Param("endDate") LocalDateTime endDate
//    );
    @Query("""
    SELECT i FROM Invoice i
    WHERE i.createdDate >= :startDate
      AND i.createdDate < :endDate
""")
    List<Invoice> findInvoicesInYear(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
