package com.example.english.Dto.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScreenRoomRequest {
    String name;
    Boolean status;
    Long cinemaId;
    Long screenRoomTypeId;
}
