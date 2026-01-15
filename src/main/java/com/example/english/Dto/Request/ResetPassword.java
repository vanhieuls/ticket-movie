package com.example.english.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPassword(
        @NotBlank(message = "Token là bắt buộc")
        String token,
        @NotBlank(message = "Vui lòng nhập mật khẩu")
        @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).+$",
                message = "Mật khẩu phải có ít nhất 1 chữ hoa và 1 ký tự đặc biệt"
        )
        String newPassword
) { }
