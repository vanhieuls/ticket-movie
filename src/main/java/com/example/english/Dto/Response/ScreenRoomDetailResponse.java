package com.example.english.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScreenRoomDetailResponse {
    Long id;
    String name;
    String status;
    String cinemaName;
    String screenRoomTypeName;
}
