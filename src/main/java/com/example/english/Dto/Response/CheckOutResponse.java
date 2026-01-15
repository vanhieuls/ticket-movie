package com.example.english.Dto.Response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckOutResponse {
    private String status;
    private String paymentUrl;
}
