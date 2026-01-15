package com.example.english.Dto.Request;

import com.example.english.Enum.CinemaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CinemaRequest {
    @NotBlank(message = "Name is Required")
    @Size( max = 200, message = "Title must be between 3 and 200 characters")
    String name;
    Long cinemaTypeId;
    @NotBlank(message = "Address is Required")
    @Size( max = 500, message = "Address must be between 3 and 500 characters")
    String address;
    Boolean status;
}
