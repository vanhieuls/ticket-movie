package com.example.english.Dto.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserRequest {
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
    int age;
}
