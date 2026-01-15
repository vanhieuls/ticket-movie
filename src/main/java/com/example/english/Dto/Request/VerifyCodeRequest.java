package com.example.english.Dto.Request;

import jakarta.validation.constraints.NotBlank;

public record VerifyCodeRequest(@NotBlank(message = "Mã Code là bắt buộc") String code) {
}
