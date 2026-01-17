package com.example.english.Repository;

import com.example.english.Entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
    Optional<Invoice> findByTxnRef(String txnRef);
    List<Invoice> findByUser_Id(Long userId);
    Optional<Invoice> findByBookingCode(String bookingCode);
}
