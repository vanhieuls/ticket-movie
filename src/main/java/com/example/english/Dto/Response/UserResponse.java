package com.example.english.Dto.Response;

import com.example.english.Enum.EnumSubset;
import com.example.english.Enum.StatusAcc;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class UserResponse {
    Long id;
    String username;
    String email;
    String phone;
    String avatar;
    String address;
    String firstName;
    String lastName;
    String dateOfBirth;
    String gender;
    StatusAcc status;
    int age;
    boolean twoFactorEnabled;
}
