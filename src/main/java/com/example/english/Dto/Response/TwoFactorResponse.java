package com.example.english.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class TwoFactorResponse {
    boolean success = false;
    String token;
    String qrCode;
}
