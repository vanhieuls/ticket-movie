package com.example.english.Repository;

import com.example.english.Entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByShowTime_Id(Long id);
    Optional<Ticket> findFirstByInvoice_Id(Long id);
    int countByInvoice_Id(Long invoiceId);

}
