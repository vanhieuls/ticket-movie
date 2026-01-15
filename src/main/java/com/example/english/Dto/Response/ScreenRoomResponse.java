package com.example.english.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScreenRoomResponse {
    Long id;
    String name;
    Boolean status;
    Long cinemaId;
    Long screenRoomTypeId;
}
