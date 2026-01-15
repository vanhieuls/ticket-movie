package com.example.english.Dto.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
public class TokenResponse {
    @JsonProperty("access_token")
    String accessToken;
    @JsonProperty("refresh_token")
    String refreshToken;
    @JsonProperty("temp_token")
    String tempToken;
    @JsonProperty("two_factor_required")
    boolean twoFactorRequired;
    @JsonProperty("authenticated")
    boolean authenticated;
}
