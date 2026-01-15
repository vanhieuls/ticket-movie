package com.example.english.Entity;
import com.example.english.Enum.EnumSubset;
import com.example.english.Enum.StatusAcc;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String username;
    String email;
    String password;
    String phone;
    String avatar;
    String address;
    String firstName;
    String lastName;
    String dateOfBirth;
    String gender;
    @EnumSubset(anyOf = {"ACTIVE", "INACTIVE", "WAITING_CONFIRM"})
    @Enumerated(EnumType.STRING)
    StatusAcc status;
    int age;
    Boolean twoFactorEnabled = false;
    Boolean nonLocked = true;
    String twoFactorSecret;
    String verificationCode;
    LocalDateTime verificationExpiresAt;
    String resetPasswordToken;
    LocalDateTime resetPasswordExpiry;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.nonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
