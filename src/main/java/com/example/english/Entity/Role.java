package com.example.english.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Role entity for Role-Based Access Control (RBAC).
 * Stores role definitions like ADMIN, STAFF, USER.
 */
@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class Role {
    @Id
    String name; // ADMIN, STAFF, USER

    String description;
}

