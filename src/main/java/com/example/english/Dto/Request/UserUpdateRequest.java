package com.example.english.Dto.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserUpdateRequest {
    String phone;
    String address;
    String firstName;
    String lastName;
    String dateOfBirth;
    String gender;
    int age;
}
