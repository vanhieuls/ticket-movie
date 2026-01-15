package com.example.english.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpecialDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    int month;
    int day;
    String description;
}
