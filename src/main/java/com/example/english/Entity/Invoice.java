package com.example.english.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
public class Invoice{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    BigDecimal totalAmount;
    LocalDate createdDate;
    LocalTime createdTime;
    String bookingCode;
    String status;
    String txnRef;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;
//    @OneToMany (mappedBy = "invoice", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
//    java.util.List<Ticket> tickets;
}
